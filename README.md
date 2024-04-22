# Building
1. mvn clean install to generate the docker image

1. (if mongodb is local) docker run 
--name rest.recipes 
--net host 
--memory="1g" 
--memory-swap="2g" 
-t rest.recipes:[version]

1. (if mongodb is local) curl -v -u craig -i 'http://localhost:9000/recipes?pageNumber=0&pageSize=10'

1. docker tag rest.recipes:[version] craigfoote/rest.recipes:[version]

1. docker push craigfoote/rest.recipes:[version]

1. docker tag rest.recipes:[version] craigfoote/rest.recipes:latest

1. docker push craigfoote/rest.recipes:latest

# Deploying

1. docker pull craigfoote/rest.recipes:latest

1. docker run 
--name rest.recipes
--net host
-d 
--restart unless-stopped 
--memory="1g" 
--memory-swap="2g" 
-t craigfoote/rest.recipes:latest 

1. curl -v -u craig -i 'http://footeware.ca:9000/recipes?pageNumber=0&pageSize=10'

# Mongodb
1. cd /opt
1. sudo mkdir data
1. cd data
1. sudo mkdir mongodb
1. cd /opt
1. sudo chmod 777 -R /opt/data
1. docker pull mongodb/mongodb-community-server:latest 
1. docker run --name mongodb -d -p 27017:27017 -v /opt/data/mongodb:/data/db mongodb/mongodb-community-server:latest