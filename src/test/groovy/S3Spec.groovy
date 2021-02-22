import spock.lang.Unroll

class S3Spec extends TerraformSpec {

    def stateBucket = 'test'

    @Unroll
    def 'creates an S3 bucket with #awsProviderVersion AWS provider and #localstackVersion LocalStack'() {
        given:
        localstack = new LocalStack(localstackVersion)
        localstack.start()
        and:
        def s3 = AWS.s3(localstack)
        s3.createBucket { it.bucket(stateBucket) }
        and:
        Terraform.Provider.generate(localstack, stateBucket, awsProviderVersion)
        Terraform.Module.generate('s3')
        Terraform.init()

        when:
        def apply = Terraform.apply()

        then:
        apply.exitValue == 0

        where:
        awsProviderVersion | localstackVersion
        '3.29.0'           | '0.12.3'
        '3.29.0'           | 'latest'
        'latest'           | '0.12.3'
        'latest'           | 'latest'
    }

}