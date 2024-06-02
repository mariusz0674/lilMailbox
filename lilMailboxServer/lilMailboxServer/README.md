# How to develop

## Windows
create catalog `C:/Docker/postgres/data`

Need docker
Wejdz w Docker/postgres
`docker build -t lil_mailbox-postgres .`
run
`docker run --name lil_mailbox_postgres -e POSTGRES_PASSWORD=q1w2e3r4 -e POSTGRES_USER=user -p 5433:5432 -v C:/Docker/postgres/data:/var/lib/postgresql/data -d lil_mailbox-postgres
`
//`docker run --name lil_mailbox_postgres -e POSTGRES_PASSWORD=q1w2e3r4 -e POSTGRES_USER=user -p 5433:5432 -v C:/Docker/postgres/data:/var/lib/postgresql/data -d postgres`

In intelij terminal `mvn clean install' ctrl + enter
Run schema user
Run schema message
run user.sql
run data.sql

Teraz Redis
`docker run --name lil_mailbox_redis -v C:/Docker/redis/data:/data -p 6379:6379 -d redis`

Mongo

`docker run --name lil_mailbox_mongo -d -p 27017:27017 -v C:/Docker/mongo/data:/data/db mongo`

czesc zapytan postman w resourceach