class Terraform {
    static init() {
        def init = Process.run('terraform init --reconfigure')
        assert init.exitValue == 0
    }

    static apply() {
        Process.run('terraform apply --auto-approve')
    }

    static class Provider {
        static generate(LocalStack localstack, String stateBucket, version = null) {
            new File('provider.tf').text = """
terraform {
  backend "s3" {
    bucket = "${stateBucket}"
    key = "terraform.tfstate"
    region = "eu-west-1"
    
    force_path_style = true
    
    endpoint = "${localstack.endpoint}"
    access_key = "${localstack.accessKey}"
    secret_key = "${localstack.secretKey}"
    skip_credentials_validation = true
  }
  
  ${version != null ? """
  required_providers {
    aws = "$version"
  }""" : ''}
}

provider "aws" {
  endpoints {
    iam = "${localstack.endpoint}"
    lambda = "${localstack.endpoint}"
    s3 = "${localstack.endpoint}"
  }
  
  region = "eu-west-1"
  
  access_key = "${localstack.accessKey}"
  secret_key = "${localstack.secretKey}"
  skip_credentials_validation = true
  skip_requesting_account_id = true
   
  s3_force_path_style = true
}"""
        }
    }

    static class Module {
        static generate(String folder) {
            new File('main.tf').text = """
module "_" {
    source = "./${folder}"
}
"""
        }
    }
}
