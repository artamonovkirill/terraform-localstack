import spock.lang.Specification

abstract class TerraformSpec extends Specification {
    protected LocalStack localstack

    def cleanup() {
        localstack?.stop()
    }
}
