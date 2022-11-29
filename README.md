# Typelevel stack backend

This project contains a small skeleton of a simple e-commerce
store using to showcase the typelevel stack. 

Libraries:
- [cats](https://typelevel.org/cats/) (Cats effect 3)
- [http4s](https://http4s.org/)
- [circe](https://circe.github.io/circe/)
- [doobie](https://tpolecat.github.io/doobie/)
- [pureconfig](https://pureconfig.github.io/)
- [log4cats](https://christopherdavenport.github.io/log4cats/)

[//]: # (- apache kafka)

SBT plugins:
- [scalariform](https://index.scala-lang.org/giabao/scalariform) for code formatting

It uses [docker-compose](https://docs.docker.com/compose/) to pack:
- a [PostgreSQL](https://www.postgresql.org/) database.
- the Application server

[//]: # (- an [Apache Kafka]&#40;https://kafka.apache.org/&#41; local instance.)

## How to run

Execute the launch script with the following command:
```bash
./scripts/run-app.sh
```

## How to use

After running the application you may access its Swagger page 
at [http://localhost:8080/api/index.html](http://localhost:8080/api/index.html)


You can try out the server by using the above swagger page or by issuing
```
> curl localhost:8080/users/1
> curl -X POST localhost:8080/users  --data '{"email":"xyz@gmail.com", "name": "TesterName", "surname": "TesterSurname}'
```
