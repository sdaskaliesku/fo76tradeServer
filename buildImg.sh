mvn clean spring-boot:build-image -Dspring-boot.build-image.imageName=mansonew2/fo76market -DskipTests=true
docker push mansonew2/fo76market:latest