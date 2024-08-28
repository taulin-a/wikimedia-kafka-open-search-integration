#!/bin/bash

mvn clean install
mvn package
java -jar ./target/wikimedia-kafka-open-search-integration-1.0-SNAPSHOT-jar-with-dependencies.jar

