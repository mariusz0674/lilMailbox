# How to develop

## Windows
create catalog `C:/Docker/postgres/data`

Need docker
run
`docker run --name lil_mailbox_postgres -e POSTGRES_PASSWORD=q1w2e3r4 -e POSTGRES_USER=user -p 5433:5432 -v C:/Docker/postgres/data:/var/lib/postgresql/data -d postgres`

In intelij terminal `mvn clean install' ctrl + enter

e vuala