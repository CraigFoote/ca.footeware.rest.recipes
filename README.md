# Building and Deploying
1. mvn clean install to generate the docker image

1. docker run 
--name ca.footeware.rest.recipes.container 
-p 9000:9000 
--net host 
--restart unless-stopped 
--memory="1g" 
--memory-swap="2g" 
-t rest.recipes:[version]

1. curl -v GET -u craig:chocolate -i 'http://localhost:9000/recipes?pageNumber=0&pageSize=10'

1. docker tag rest.recipes:[version] craigfoote/rest.recipes:[version]

1. docker push craigfoote/rest.recipes:[version]

1. docker tag rest.recipes:[version] craigfoote/rest.recipes:latest

1. docker push craigfoote/rest.recipes:latest

1. docker run 
--name ca.footeware.rest.recipes.container 
-p 9000:9000 
--net host
-d 
--restart unless-stopped 
--memory="1g" 
--memory-swap="2g" 
-t craigfoote/rest.recipes:latest 

1. curl -v GET -u craig -i 'http://localhost:9000/recipes?pageNumber=0&pageSize=10'
