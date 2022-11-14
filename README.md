# match-service

 Service provide following endpoints: 
 ```
 /v1/matches GET 
 /v1/matches POST
 /v1/matches/{matchID} PATCH
 /v1/matches/{matchID} DELETE
 
 /v1/teams GET
 /v1/teams PUT
 ```


## Build and Run service on port 8080 
```
 1) mvn clean package
 2) java -jar target/service-match-0.0.1-SNAPSHOT.jar
```
##Try it out
<href>http://localhost:8080/swagger-ui/index.html





