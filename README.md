# Sentiment Facebook Data Transformation

## Introduction

Simple Java application for consuming and transforming posts' comments from given FB page. Provide flexible way for configuration through properties files:
- application.properties
- facebook4j.properties

Consists of Unit tests for validating the FbDataTransformer.

## Getting Started

- `mvn install`
- `java -jar target/fb-data-transformation-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

## Used libraries

- facebook4j-core
- gson
- jedis
- log4j
- commons-lang3
- junit
