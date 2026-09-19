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

### run a postgres instance from a container using docker compose

#### this instance will run a script that automatically create a 2 user setup with least privileges possible for each

#### 1. One user for DDL operations only, for use by flyway

#### 2. Another user for CRUD operations only, for use by the spring app

```bash
docker compose up -d
```

### Then run the app

```bash
./mvnw spring-boot:run
```

### API documentation swagger ui

````http
http://localhost:8081/swagger-ui/index.html
````

### To run tests

```bash
./mvnw test
```

# TODO

### 1. Merge this repo into a monorepo of microservices that communicate through either sync or async communication

### 2. Use UUIDv7 for orderNumber

### 3. Create OrderItem with many-to-one relationship with Order