# Terraform with LocalStack [![Build Status](https://travis-ci.com/artamonovkirill/terraform-localstack.svg?branch=master)](https://travis-ci.com/artamonovkirill/terraform-localstack)

This repository is a test of Terraform AWS provider integration with LocalStack.

## Building

Build the project (including running the tests):
```
./gradlew build
```

Build and publish the results to Gradle Build Scans: 
```
./gradlew build --scan
```

At the end of the build output, you'll have a link like [https://gradle.com/s/563fobrm24i7c](https://gradle.com/s/563fobrm24i7c) with your build details.