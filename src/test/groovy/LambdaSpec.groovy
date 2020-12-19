import spock.lang.Unroll

class LambdaSpec extends TerraformSpec {

    def stateBucket = 'test'

    @Unroll
    def 'invokes a lambda with #awsProviderVersion AWS provider and #localstackVersion LocalStack'() {
        given:
        localstack = new LocalStack(localstackVersion)
        localstack.start()
        and:
        def s3 = AWS.s3(localstack)
        s3.createBucket { it.bucket(stateBucket) }
        and:
        Terraform.Provider.generate(localstack, stateBucket, awsProviderVersion)
        Terraform.Module.generate('lambda')
        Terraform.init()
        and:
        def lambda = AWS.lambda(localstack)

        when:
        def apply = Terraform.apply()

        then:
        apply.exitValue == 0

        when:
        def result = lambda.invoke { it.functionName('test') }

        then:
        result.statusCode() == 200
        !result.functionError()

        where:
        awsProviderVersion | localstackVersion
        '2.70.0'           | '0.12.3'
        '2.70.0'           | 'latest'
        '3.18.0'           | '0.12.3'
        '3.18.0'           | 'latest'
        '3.21.0'           | '0.12.3'
        '3.21.0'           | 'latest'
        'latest'           | '0.12.3'
        'latest'           | 'latest'
    }

}
