# Odpalenie developerskie
odpalić bazki i ustawić namiary w application yaml
można na jednym mongo.
uruchomi się na porcie 8080
# Wymaga uruchomionego Docker Desktop. Na linuxie nie sprawdzane
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
