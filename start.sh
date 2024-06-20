#!/bin/bash

cd frontend 

npm run build

cd ..

cp -r frontend/public/. src/main/resources/META-INF/resources/

./mvnw compile quarkus:dev