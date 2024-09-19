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
</style>

# Preparation
1. `docker network create recipes-network`
1. `sudo mkdir -p /opt/mongodb/data`
1. `sudo chmod 777 /opt/mongodb/data`
1. `docker run -d --name mongodb -p 27017:27017 --network recipes-network -v /opt/mongodb/data:/data/db mongo:8.0.0-rc20-noble`
1. populate mongodb using Mongo Compass
1. `sudo mkdir -p /opt/mongodb/data`
1. `sudo chmod -R 777 /opt/rest.recipes/`

# Development
1. `mvn clean package`
1. `docker run -d --name rest.recipes -p 9000:9000 --network recipes-network -v /opt/rest.recipes/logs:/opt/rest.recipes/logs -t rest.recipes:[version]`
1. `curl -v -u craig -i 'http://localhost:9000/recipes?pageNumber=0&pageSize=10'`

# Release
1. commit changes to git and create tag [version]
1. `docker tag rest.recipes:[version] craigfoote/rest.recipes:[version]`
1. `docker push craigfoote/rest.recipes:[version]`
1. `docker tag rest.recipes:[version] craigfoote/rest.recipes:latest`
1. `docker push craigfoote/rest.recipes:latest`
