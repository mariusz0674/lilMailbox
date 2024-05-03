# How to develop

## Windows
create catalog `C:/Docker/postgres/data`

Need docker
run
`docker run --name lil_mailbox_postgres -e POSTGRES_PASSWORD=q1w2e3r4 -e POSTGRES_USER=user -p 5433:5432 -v C:/Docker/postgres/data:/var/lib/postgresql/data -d postgres`

In intelij terminal `mvn clean install' ctrl + enter
Run schema user
Run schema message
run user.sql
run data.sql

Teraz mini <3

`docker run -p 9000:9000 -p 9001:9001 --name lil_mailbox_minio -v C:/Docker/minio/data:/data -e "MINIO_ROOT_USER=user" -e "MINIO_ROOT_PASSWORD=q1w2e3r4" quay.io/minio/minio server /data --console-address ":9001"`
http://127.0.0.1:9001/
bucket: lilmailbox

Teraz Redis
`docker run --name lil_mailbox_redis -v C:/Docker/redis/data:/data -p 6379:6379 -d redis`

e vuala

czesc zapytan postman w resourceach