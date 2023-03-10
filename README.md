
# π 1μ‘° λ¦¬λ°μ΄λ 
>**βκ³ κ°, νλ§€μ λ§€μΉ­ κ±°λ μλΉμ€β like π₯**

<div>
 <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white">
 <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white">
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
 <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
 <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white">
</div>
<div>
 <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
 <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white">
 </div>
<div>
 <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white"><img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
</div>

## λͺ©μ°¨

<!-- TOC -->
* [π» νλ‘μ νΈ κ°λ° νκ²½](#π»)
* [π₯Β νμ μκ°](#νμ-μκ°)
    * [μ­ν ](#μ­ν )
* [νλ‘μ νΈ μκ΅¬μ¬ν­](#νλ‘μ νΈ-μκ΅¬μ¬ν­)
* [Usecase](#usecase)
* [Table ERD](#table-erd)
* [Class UML](#class-uml)
* [API λͺμΈ](#api-λͺμΈ)
<!-- TOC -->


<br>


## π»
<details><summary> &nbsp νλ‘μ νΈ κ°λ° νκ²½</summary>

- spring 2.7.7

- JDK 11
- build.gradle
    ```
   dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        implementation 'org.springframework.boot:spring-boot-starter-security'
        implementation 'org.springframework.boot:spring-boot-starter-web'
        implementation 'org.springframework.boot:spring-boot-starter-validation'
    
        compileOnly 'org.projectlombok:lombok'
        runtimeOnly 'com.h2database:h2'
        annotationProcessor 'org.projectlombok:lombok'
    
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testImplementation 'org.springframework.security:spring-security-test'
    
        testCompileOnly 'org.projectlombok:lombok'
        testAnnotationProcessor 'org.projectlombok:lombok'
    
    
        compileOnly group: 'io.jsonwebtoken', name: 'jjwt-api', version: '0.11.2'
        runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-impl', version: '0.11.2'
        runtimeOnly group: 'io.jsonwebtoken', name: 'jjwt-jackson', version: '0.11.2'
    
        implementation 'org.springframework.boot:spring-boot-starter-data-redis'
        implementation group: 'it.ozimov', name: 'embedded-redis', version: '0.7.1'
    
        implementation 'org.springframework.boot:spring-boot-starter-websocket'
    }
    ```

- application.properties

  ```
   spring.jpa.hibernate.ddl-auto=create
    spring.jpa.generate-ddl=true
    
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.highlight_sql=true
    logging.level.org.hibernate.SQL=debug
    logging.level.org.hibernate.type.descriptor.sql=trace
    
    spring.h2.console.enabled=true
    spring.datasource.url=jdbc:h2:mem:db;MODE=MYSQL;
    spring.datasource.username=sa
    
    jwt.secret.key=7ZWt7ZW0OTntmZTsnbTtjIXtlZzqta3snYTrhIjrqLjshLjqs4TroZzrgpjslYTqsIDsnpDtm4zrpa3tlZzqsJzrsJzsnpDrpbzrp4zrk6TslrTqsIDsnpA=
    
    spring.redis.host=localhost
    spring.redis.port=6379
    
    profile.default.image.path=/Users/sj/Downloads/default_profile.png
    profile.image.dir=/Users/sj/Downloads/user_profile_image/
    ```
</details>
<br>

## π₯Β νμ μκ°
μ΄μν, μ΄μ‘μΈ, μ΄μ ν¬, μ₯μ±μ€, μ‘°μ±μ 

### μ­ν 

| λ΄λΉμ | μ­ν                                                                           |
|:---:|:----------------------------------------------------------------------------|
|     |                                                                             |
| μ΄μν | - μ μ  μ‘°ν<br/>- νλ§€μ κΆν μμ²­/μΉμΈ/μ­μ <br/>- κΆν μμ²­ λͺ©λ‘ μ‘°ν                             |
| μ΄μ‘μΈ | -  μνλ¦¬ν°<br/>- νμκ°μ / λ‘κ·ΈμΈ<br/>-  λ‘κ·Έμμ<br/>- νλ‘ν μ€μ /μ‘°ν<br/>- λ¦¬λλ―Έ μμ±           |
| μ΄μ ν¬ | - μ μ²΄ νλ§€ μν μ‘°ν<br/>- νλ§€μ λ±λ‘ μν μ‘°ν/κ²μ/λ±λ‘/μμ /μ­μ (νλ§€μ ν¬ν¨)<br/>- μ±ν<br/>- νλ‘μ νΈ λ°ν |
| μ₯μ±μ€ | - μνλ¦¬ν°(λ¦¬νλ μ ν ν°)<br/>-  λ λμ€ μ μ©<br/>- μΉ΄νκ³ λ¦¬ μ‘°ν/μμ±/μμ /μ­μ                         |
| μ‘°μ±μ  | - κ±°λ μ‘°ν/μμ²­/μλ½<br/>- νλ§€μ μ‘°ν<br/>- νλ‘ν μ΄λ―Έμ§ μ μ₯/μ‘°ν<br/>- μμ° μμ μ μ               |


<br>

## νλ‘μ νΈ μκ΅¬μ¬ν­
<details><summary> λͺμΈ
</summary>- μ°λ¦¬νλ§μ λ§€μΉ­ μλΉμ€ νλ‘μ νΈ λ§λ€κΈ°
[ κ³ κ°-νλ§€μ λ§€μΉ­ μλΉμ€ (λ§€μΉ­μ£Όμ  μμ ) ]

- νμκ°μ/λ‘κ·ΈμΈ/λ‘κ·Έμμ/ν ν° κΈ°λ₯
- μ μ  κΆν κΈ°λ₯
    - μ μ λ 3κ°μ§ κΆνμΌλ‘ λλ©λλ€.
        - κ³ κ° : μ΅μ΄ νμκ°μν μ μ 
        - νλ§€μ : νλ§€μ μΉμΈμ λ°μ κ³ κ°
        - μ΄μμ : νλ§€μ μΉμΈμ ν΄μ£Όλ μ μ 
- μ μ  κΆν λ³ κΈ°λ₯
    - κ³ κ°
        - μ‘°ν
            - λμ νλ‘ν μ€μ  λ° μ‘°ν : μ μ λ³ νλ‘ν(λλ€μ, μ΄λ―Έμ§)μ μ€μ ν  μ μκ³  μ‘°ν
            - μ μ²΄ νλ§€μν λͺ©λ‘ : νλ§€ μνλͺ©λ‘μ νμ΄μ§νλ©° μ‘°ν
            - μ μ²΄ νλ§€μ λͺ©λ‘ : νλ§€μλ€μ λͺ©λ‘μ νμ΄μ§νλ©° μ‘°ν
            - νλ§€μ μ λ³΄ : νλ§€μλ₯Ό μ νν΄μ νλ‘ν μ λ³΄(λλ€μ,μ΄λ―Έμ§,μκ°κΈ+λ§€μΉ­μ£Όμ  μ λ³΄)λ₯Ό μ‘°ν
        - μμ±
            - νλ§€μμκ² μμ²­νΌ : νλ§€μμκ² μμ²­λ΄μ©(λ§€μΉ­μ£Όμ  μ λ³΄) λ³΄λ΄κΈ°
        - κΆν μμ²­
            - νλ§€μ λ±λ‘ μμ²­ : νλ§€μ νλ‘ν μμ²­ μ λ³΄λ₯Ό μμ±ν΄μ μ΄μμμκ² νλ§€μ λ±λ‘ μμ²­
    - νλ§€μ
        - μ‘°ν
            - λμ νλ§€μ νλ‘ν μ€μ  λ° μ‘°ν : νλ§€μλ³ νλ‘ν(λλ€μ,μ΄λ―Έμ§,μκ°κΈ+λ§€μΉ­μ£Όμ  μ λ³΄)μ μ€μ , μ‘°ν
            - λμ νλ§€μν μ‘°ν : λ΄κ° νλ§€μ€μΈ μν λͺ©λ‘μ νμ΄μ§νλ©° μ‘°ν
            - κ³ κ°μμ²­ λͺ©λ‘ μ‘°ν : λͺ¨λ μνμ κ³ κ°μμ²­ λͺ©λ‘μ νμ΄μ§νλ©° μ‘°ν
        - λ±λ‘
            - λμ νλ§€μν λ±λ‘ : νλ§€ μν μ λ³΄λ₯Ό μμ±νμ¬ λͺ©λ‘μ λ±λ‘
        - μμ 
            - λμ νλ§€μν μμ /μ­μ  : νλ§€ μν μ λ³΄λ₯Ό μμ±νμ¬ λͺ©λ‘μμ μμ 
        - μ­μ 
            - λμ νλ§€μν μ­μ  : νλ§€ μν μ λ³΄λ₯Ό μμ±νμ¬ λͺ©λ‘μμ μ­μ 
        - κ³ κ°μμ²­ μ²λ¦¬ : κ³ κ°μμ²­μ μλ½νκ³  μλ£μ²λ¦¬
    - μ΄μμ
        - μ‘°ν
            - κ³ κ° λͺ©λ‘ : κ³ κ°λ€μ λͺ©λ‘μ νμ΄μ§νλ©° μ‘°ν
            - νλ§€μ λͺ©λ‘ : νλ§€μλ€μ λͺ©λ‘μ νμ΄μ§νλ©° μ‘°ν
            - νλ§€μ λ±λ‘ μμ²­νΌ λͺ©λ‘ : νλ§€μ λ±λ‘ μμ²­λͺ©λ‘μ μ‘°ν
        - κΆν λ±λ‘
            - νλ§€μ κΆν μΉμΈ : νλ§€μ λ±λ‘ μμ²­μ μΉμΈ
        - μ­μ 
            - νλ§€μ κΆν : μ μ μ νλ§€μ κΆνμ μ­μ 
- κ²μ κΈ°λ₯
    - ν€μλ κ²μ : νμ΄μ§ λͺ©λ‘ μ‘°νλ₯Ό ν λ κ²μ ν€μλλ₯Ό μλ ₯ν΄ κ²μνλ κΈ°λ₯μ μΆκ°ν΄λ³΄μΈμ.
    - νλ§€μ κ²μ : νμ΄μ§ λͺ©λ‘ μ‘°νλ₯Ό ν λ νλ§€μλͺμΌλ‘ κ²μνλ κΈ°λ₯μ μΆκ°ν΄λ³΄μΈμ.


- κ³ κ°-νλ§€μ λν κΈ°λ₯
    - λνλ°© μμ± : νλ§€κ° μμλ λ λνλ°©μ΄ μμ±λλ€.
    - λν λ©μΈμ§ μ μ‘κΈ°λ₯ : κ³ κ°κ³Ό νλ§€μκ° νλ§€κ±΄μ λν λνλ₯Ό λλλ€.
    - λνλ°© λ©μΈμ§ λͺ©λ‘ μ‘°ν : κ³ κ°κ³Ό νλ§€μκ° λλ λνλͺ©λ‘μ μ‘°νν  μ μλ€.
    - λνλ°© μ’λ£ : νλ§€κ° μλ£λ λ λνλ°©μ΄ μ€μ§λκ³  λμ΄μ λ©μΈμ§ μ μ‘μ΄ λΆκ°λ₯νλ€.
</details>

<br>

## Usecase
![Usecase.png](document/usecase.png)

<br>

## Table ERD
![TableERD.png](document/TableERD.png)

<br>

## Class UML
![ClassUML.png](document/ClassUML.png)

## API λͺμΈ
![img.png](document/UserAPI.png)

![img.png](document/AdminAPI.png)

![img.png](document/ItemAPI.png)

![img.png](document/TransactionAPI.png)

![img.png](document/CategoryAPI.png)

![img.png](document/ChatAPI.png)













