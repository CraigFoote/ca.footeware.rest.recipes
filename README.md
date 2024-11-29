<style>
body {
	background-color: #2c2c2c;
}
h1,h2,h3 {
	color: #5d85df;
}
li {
	font-family: 'FiraCode Nerd Font Light';
	font-size: 18px;
	color: #dddddd;
}
code {
	color: #dbc41c;
}
a {
	color: #0da6fa;
}
</style>

# Preparation
1. `docker network create recipes-network`
1. `sudo mkdir -p /opt/mongodb/data`
1. `sudo chmod 777 /opt/mongodb/data`
1. `docker run -d --name mongodb -p 27017:27017 --network recipes-network -v /opt/mongodb/data:/data/db mongo:latest`
1. populate mongodb using Mongo Compass
1. `sudo mkdir -p /opt/mongodb/data`
1. `sudo chmod -R 777 /opt/rest.recipes/`

# Development Certificate
1. `openssl req -newkey rsa:2048 -keyout footeware.ca.test.key -x509 -days 365 -out footeware.ca.test.crt`
1. `openssl rsa -in footeware.ca.test.key -aes256 -out aes.pem`
1. Place cert, key and its AES version in /src/main/resources.

# Building
1. `mvn clean package` or use the `-BUILD` eclipse launch config
1. run as Spring Boot app using `-RUN` eclipse launch config. It uses the `-dev` profile that uses the self-signed cert.
1. NOT WORKING: [https://localhost:9000/recipes?pageNumber=0&pageSize=10](https://localhost:9000/recipes?pageNumber=0&pageSize=10)
1. `docker run -d --name rest.recipes -p 9000:9000 --network recipes-network -v /opt/rest.recipes/logs:/opt/rest.recipes/logs -t rest.recipes:[version]`
1. `curl -v -u craig -i 'http://localhost:9000/recipes?pageNumber=0&pageSize=10'`

# Releasing
1. commit changes to git and create tag [version]
1. `docker tag rest.recipes:[version] craigfoote/rest.recipes:[version]`
1. `docker push craigfoote/rest.recipes:[version]`
1. `docker tag rest.recipes:[version] craigfoote/rest.recipes:latest`
1. `docker push craigfoote/rest.recipes:latest`
