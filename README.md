# Terraform with LocalStack ![Gradle](https://github.com/artamonovkirill/terraform-localstack/workflows/Gradle/badge.svg)

This repository is a test of Terraform AWS provider integration with LocalStack.

It is used for:

* early detection of version incompatibilities
* a source of compatible versions

between Terraform, Terraform AWS provider and LocalStack

## Known (past) incompatibilities

* [Terraform's latest aws_provider release crashes on localstack](https://github.com/localstack/localstack/issues/3297): 
  LocalStack versions prior to 0.12.3 with Terraform AWS provider 3.18.0+
* [Bucket HEAD request is not properly forwarded to S3 through edge router](https://github.com/localstack/localstack/issues/2832)
  LocalStask version prior to 0.11.5 for Terraform AWS provider 3.0.0+

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