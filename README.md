# Orders microservice

## Run locally

### clone repo

```bash
git clone https://github.com/Mark-GM/order-service-spring-boot
cd order-service-spring-boot
```

### use example .env file locally

```bash
mv .env.example .env
```

### run a postgres instance from a container

#### this instance will run a script that automatically create a 2 user setup with least privileges possible for each

#### 1. One user for DDL operations only and used by flyway

#### 2. Another user for CRUD operations only and used by the spring app

```bash
docker compose up -d
```
