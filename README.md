# Preparation

1. `docker network create recipes-network`
1. `sudo mkdir -p /opt/mongodb/data`
1. `sudo chmod 777 /opt/mongodb/data`
1. `sudo mkdir -p /opt/rest.recipes`
1. `sudo chmod -R 777 /opt/rest.recipes`
1. Add `127.0.0.1 mongodb` to `/etc/hosts` file.
1. `docker run -d --name mongodb -p 27017:27017 --network recipes-network -v /opt/mongodb/data:/data/db mongo:latest`

# Populate mongodb

1. using Mongo Compass, connect to `mongodb://localhost:27017`
1. click the **Create Database** + button
1. enter `recipes` for db name and `recipe` (singular) for the collection name
1. select `recipe` collection at left.
1. click the **Add Data** button and **Import JSON or CSV file**
1. browse to `recipe.json`
1. select `recipes` db at left
1. click **Create collection** button at right
1. enter `recipeImage` as collection name
1. browse to `recipeImage.json`
	
## Development Certificate

1. `openssl req -newkey rsa:2048 -keyout footeware.ca.test.key -x509 -days 365 -out footeware.ca.test.crt`
1. `openssl rsa -in footeware.ca.test.key -aes256 -out aes.pem`
1. place cert, key and its AES version in /src/main/resources.

# Building
1. `mvn clean package` or use the `-BUILD` eclipse launch config
1. run as Spring Boot app using `-RUN` eclipse launch config. It uses the `-dev` profile that uses the self-signed cert.
1. check content at [https://localhost:9000](https://localhost:9000) (click Advanced and Proceed to allow self-signed cert)
1. stop spring boot app and start docker container: `docker run -d --name rest.recipes -e "SPRING_PROFILES_ACTIVE=dev" -p 9000:9000 --network recipes-network -v /opt/rest.recipes/logs:/opt/rest.recipes/logs -t rest.recipes:[version]`
1. again check content at [https://localhost:9000](https://localhost:9000)

# Releasing
1. commit changes to git and create tag [version]
1. `docker tag rest.recipes:[version] craigfoote/rest.recipes:[version]`
1. `docker push craigfoote/rest.recipes:[version]`
1. `docker tag rest.recipes:[version] craigfoote/rest.recipes:latest`
1. `docker push craigfoote/rest.recipes:latest`
