mvn clean spring-boot:build-image -DskipTests=true -Dspring-boot.build-image.imageName=mansonew2/fo76market
docker push mansonew2/fo76market:latest