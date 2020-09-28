data "archive_file" "zip" {
  type = "zip"
  source_file = "./index.js"
  output_path = "lambda.zip"
}

resource "aws_iam_role" "_" {
  name = "test"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_lambda_function" "_" {
  function_name = "test"
  handler = "index.handler"

  filename = "lambda.zip"

  role = aws_iam_role._.arn

  runtime = "nodejs12.x"
}