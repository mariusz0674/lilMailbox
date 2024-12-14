# Odpalenie developerskie
odpalić kontenery
`docker run --name lil_mailbox_mongo -d -p 27017:27017 -v mongo-data:/data/db mongo`
`docker run --name lil_mailbox_redis -v redis-data:/data -p 6379:6379 -d redis`
`docker run --name lil_mailbox_postgres -p 5433:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -v postgres-data:/var/lib/postgresql/data postgres`

odpalic apke
uruchomi się na porcie 8083

# Odpalenie compose z lokalną apką
- wymaga jdk 17
- maven

``` mvn clean package -DskipTests```
zbuduje jarka
następnie

odkomentować
    
    build: . 
zakomentować

    image: mariusz0674/lil-mailbox-server:latest
(uzycie w compose apki zbudowanej lokalnie)

odpalenie

```docker-compose up --build```

# Odpalenie przy użuciu docker repository
- wymagania: odpalony docker desktop

po prostu

```docker-compose up --build```

apka dostępna na porcie :8083






