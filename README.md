# Building
1. mvn clean install to generate the docker image

1. (if mongodb is local) docker run 
--name rest.recipes 
--net host 
--memory="1g" 
--memory-swap="2g" 
-t rest.recipes:[version]

1. (if mongodb is local) curl -v GET -u craig -i 'http://localhost:9000/recipes?pageNumber=0&pageSize=10'

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

1. curl -v GET -u craig -i 'http://footeware.ca:9000/recipes?pageNumber=0&pageSize=10'