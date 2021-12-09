package:
	mvn clean package

build-image:
	docker image build -t clines-api .

container-run:
	docker container run --name clines-api -d -p 8080:8080  clines-api

remove-image:
	docker container rm clines-api
	docker image rm clines-api
	
rebuild-image: remove-image
	make build-image

start:
	docker container start clines-api

stop:
	docker container stop clines-api
	
logs:
	docker container logs clines-api