#!/usr/bin/env bash
set -euo pipefail

# 1. VALIDATION: Check for required environment variables
function check_env_vars() {
  local -a REQUIRED_VARS=(
  POSTGRES_USER
  POSTGRES_PASSWORD
  MIGRATION_USER
  MIGRATION_PASSWORD
  APP_USER
  APP_PASSWORD
  POSTGRES_DB)
  local -a errors=()

#  for var in $REQUIRED_VARS; do
  for var in "${REQUIRED_VARS[@]}"; do
    declare -n ref="$var"

    if [ -z "${ref+x}" ] || [ -z "$ref" ]; then
          errors+=("$var is unset or empty. Provide it in your environment.")
    fi
  done

  if [ ${#errors[@]} -gt 0 ]; then
    printf '%s\n' "${errors[@]}" >&2
    exit 1
  fi
}

check_env_vars

# 2. EXECUTION: Run psql with internal variables
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
  -v mig_user="$MIGRATION_USER" \
  -v mig_pass="$MIGRATION_PASSWORD" \
  -v app_user="$APP_USER" \
  -v app_pass="$APP_PASSWORD" \
  -v app_db="$POSTGRES_DB"<<-EOSQL
  SELECT format('CREATE USER %I WITH LOGIN PASSWORD %L NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION CONNECTION LIMIT 5', :'mig_user', :'mig_pass')
      WHERE NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = :'mig_user') \gexec

  SELECT format('CREATE USER %I WITH LOGIN PASSWORD %L NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION CONNECTION LIMIT 50', :'app_user', :'app_pass')
      WHERE NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = :'app_user') \gexec
  -- Minimalist Hardening
  REVOKE ALL ON DATABASE postgres FROM PUBLIC;
  REVOKE ALL ON DATABASE template0 FROM PUBLIC;
  REVOKE ALL ON DATABASE template1 FROM PUBLIC;
  REVOKE CONNECT ON DATABASE postgres FROM :"app_user", :"mig_user";
  REVOKE CONNECT ON DATABASE template0 FROM :"app_user", :"mig_user";
  REVOKE CONNECT ON DATABASE template1 FROM :"app_user", :"mig_user";

  -- Too permissive: ALTER DATABASE :"app_db" OWNER TO :"mig_user";
  REVOKE ALL ON DATABASE :"app_db" FROM PUBLIC;
  REVOKE CREATE ON SCHEMA public FROM :"app_user";
  REVOKE ALL ON SCHEMA public FROM PUBLIC;

  -- Grant mig_user ability to connect and use the database
  -- Grant mig_user ability to create schemas (needed if migrations create new schemas)
  GRANT CONNECT, TEMPORARY, CREATE ON DATABASE :"app_db" TO :"mig_user";
  -- Transfer ownership of the public schema to mig_user
  ALTER SCHEMA public OWNER TO :"mig_user";

  GRANT CONNECT ON DATABASE :"app_db" TO :"app_user";
  GRANT USAGE ON SCHEMA public TO :"app_user";

  -- Default Privileges for Flyway migrations
  ALTER DEFAULT PRIVILEGES FOR ROLE :"mig_user" IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO :"app_user";
  ALTER DEFAULT PRIVILEGES FOR ROLE :"mig_user" IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO :"app_user";
  -- Default privileges: functions and procedures
  ALTER DEFAULT PRIVILEGES FOR ROLE :"mig_user" IN SCHEMA public
    GRANT EXECUTE ON ROUTINES TO :"app_user";

  SELECT format('Database and Roles Setup Complete: Database %I is now used by %I for migrations and DDL operations and by %I for DML operations.',
                    :'app_db', :'mig_user', :'app_user') AS init_script_status;
EOSQL
