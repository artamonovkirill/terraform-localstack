import spock.lang.Specification
import spock.lang.Unroll

class S3Spec extends Specification {

    @Unroll
    def 'creates an S3 bucket with #awsProviderVersion AWS provider and #localstackVersion LocalStack'() {
        given:
        def localstack = new LocalStack(localstackVersion)
        localstack.start()
        and:
        Terraform.Provider.generate(localstack, awsProviderVersion)
        Terraform.init()

        when:
        def apply = Terraform.apply()

        then:
        apply.exitValue == 0

        cleanup:
        localstack.stop()

        where:
        awsProviderVersion | localstackVersion
        '2.70.0'           | '0.11.3'
        '2.70.0'           | 'latest'
        '3.1.0'            | '0.11.5'
        '3.8.0'            | '0.11.5'
    }

    @Unroll
    def 'fails to create an S3 bucket with #awsProviderVersion AWS provider and #localstackVersion LocalStack'() {
        given:
        def localstack = new LocalStack(localstackVersion)
        localstack.start()
        and:
        Terraform.Provider.generate(localstack, awsProviderVersion)
        Terraform.init()

        when:
        def apply = Terraform.apply()

        then:
        apply.exitValue != 0

        cleanup:
        localstack.stop()

        where:
        awsProviderVersion | localstackVersion
        '3.0.0'            | '0.11.3' // https://github.com/localstack/localstack/issues/2832
        '3.8.0'            | '0.11.3' // https://github.com/localstack/localstack/issues/2832
        '3.1.0'            | '0.11.4' // https://github.com/localstack/localstack/issues/2832
        '3.8.0'            | '0.11.4' // https://github.com/localstack/localstack/issues/2832
        '3.0.0'            | 'latest'
        '3.8.0'            | 'latest'
    }

}