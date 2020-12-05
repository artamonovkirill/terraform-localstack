import spock.lang.Unroll

class LambdaSpec extends TerraformSpec {

    def stateBucket = 'test'

    @Unroll
    def 'invokes a lambda with #version LocalStack'(version) {
        given:
        localstack = new LocalStack(version)
        localstack.start()
        and:
        def s3 = AWS.s3(localstack)
        s3.createBucket { it.bucket(stateBucket) }
        and:
        Terraform.Provider.generate(localstack, stateBucket)
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
        version << [
                // '0.11.5', // everything below 0.12.2, see https://github.com/localstack/localstack/issues/3297
                'latest']
    }

}
