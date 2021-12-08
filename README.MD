# Projeto com os exercícios dos cursos de agilidade com Java da plataforma Alura

Instrutor: Rodrigo Ferreira

## Agilidade e TDD: Um dia no desenvolvimento de software

Link para o curso: https://www.alura.com.br/curso-online-agilidade-tdd-desenvolvimento-software

## Agilidade e DevOps: Um dia no desenvolvimento de software

Link para o curso: https://www.alura.com.br/curso-online-agilidade-devops-desenvolvimento-software

# Requisitos

- Java 11

# Para Rodar

```
mvn clean package

docker image build -t clines-api .

docker container run --name clines-api -d -p 8080:8080  clines-api
```
