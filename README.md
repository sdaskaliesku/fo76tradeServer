# Fallout 76 trade server application

# Setup

Run `mvn clean package` to build both backend and front-end

## Running

1. Setup system properties:

MongoDB config:

`mongo.user` - db user

`mongo.password` - db password

`mongo.db` - db name

`mongo.url` - db url

`jwt.secret` - secret for jwt generation

e.g. `-Dmongo.user=<db user> -Dmongo.password=<db user passsword> -Dmongo.db=<db name> -Dmongo.url=<url> -Djwt.secret=<secret>`

2. Run Main class

3. [Optional] Run `watch` script in `package.json` to watch changes in `ui/` folder and re-build frontend automatically

3. Visit http://localhost:8080/
