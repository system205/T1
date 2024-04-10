[![Super-Linter](https://github.com/<OWNER>/<REPOSITORY>/actions/workflows/lint.yaml/badge.svg)](https://github.com/marketplace/actions/super-linter)
# Solved homeworks for T1 school 2024

## Description

An example of communication between 2 microservices called [consumer](/task1/consumer) and [supplier](/task1/supplier)
via [RestTemplate](https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html)

## Features
- Error handling
- CRUD RESTful API for [Categories](/task1/supplier/src/main/java/study/supplier/entity/Category.java) 
and [Product](/task1/supplier/src/main/java/study/supplier/entity/Product.java) entities
- Filtering by price, name, description, category with [QueryDSL](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa)
- Pagination (page and size)
- Dockerization with docker-compose and PostgreSQL

## Run
```bash
docker-compose up
```
See http://localhost:8080/api/v1

