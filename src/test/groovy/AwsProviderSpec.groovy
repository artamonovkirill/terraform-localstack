import spock.lang.Specification
import spock.lang.Unroll

class AwsProviderSpec extends Specification {

    def localstack = new LocalStack()

    def setup() {
        localstack.start()
    }

    @Unroll
    def 'creates an S3 bucket with #version AWS provider'() {
        given:
        Terraform.Provider.generate(localstack, version)
        Terraform.init()

        when:
        def apply = Terraform.apply()

        then:
        apply.exitValue == 0

        where:
        version << ['2.70.0', '3.0.0', '3.1.0']
    }

    def cleanup() {
        localstack.stop()
    }

}