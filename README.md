# sermaluc

## Download
```
git clone https://github.com/spring-guides/gs-spring-boot.git
```
## Running as a packaged application
```
mvn install
java -jar target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar
```
## Run using the Maven plugin
```
mvn spring-boot:run
```
## API Documentation (Swagger)
```
http://localhost:8080/swagger-ui/index.html
```
## Test Success
Ejecutar una petición POST a la URL "http://localhost:8080/user/register" con Media Type "application/json" con la siguiente estructura

Example
```
{
"name": "fretamal",
"email": "fretamal@mail.com",
"password": "12345678",
"phones": [
{
"number": "900000000",
"cityCode": "2",
"contryCode": "56"
}
]
}
```