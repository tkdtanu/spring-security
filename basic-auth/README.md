# Http Basic Auth

Dependency
-----------------------------
Add the correct dependency on POM
    
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
   You might need other Dependencies as well like `starter-web` and others for test case.
   
   Please check the POM file for all the reference.
   
Enable Spring Security
---------------------
We have to add the Annotation `@EnbaleWebSecurity`. <br>
Have to override `configure(HttpSecurity http)` method and add your logic for enabling Auth.<br>
Check `SecurityConfig` file for clear info.

Set Default User/Password for Spring Security
---------------------------------------------
If we didn't set the defualt username & password, then spring will generate a random password. And username will be `user`.<br>
But in this project we have set the default username & password, so that will help to write test also.<br>
Because if spring generate a random password we will not be able to get that and set on test case.<br>

Below are the property which sets the default username and password for spring security.
```
spring.security.user.name=user
spring.security.user.password=password
```

APIs
-----
In this Project There are three Apis Available

1. http://localhost:7001/private/welcome -->This API need Authorization. Have to pass `username` & `password` headers.
2. http://localhost:7001/public/welcome -->This is a public API. Which doesn't need any authorization Header.
3. http://localhost:7001/welcome --> This is a normal API. (This needs Authorization header, as we mentioned every other request except `/private**` & `/public/**` should be authenticated. We Have mentioned this on `SecurityConfig` file)

Curl Command for All the APIs
------------------------------
1.For first API (http://localhost:7001/private/welcome)
```
curl --location --request GET 'http://localhost:7001/private/welcome' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```
2.For second API (http://localhost:7001/public/welcome)
```
curl --location --request GET 'http://localhost:7001/public/welcome'
```
3.For Third API (http://localhost:7001/welcome)
```
curl --location --request GET 'http://localhost:7001/welcome' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

 