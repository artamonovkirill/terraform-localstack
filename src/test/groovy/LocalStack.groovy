import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

class LocalStack {
    final GenericContainer container
    final accessKey = 'foo'
    final secretKey = 'bar'
    final region = 'eu-west-1'

    LocalStack(version) {
        container = new GenericContainer("localstack/localstack:$version")
                .withEnv(SERVICES: 's3', DEFAULT_REGION: region)
                .withExposedPorts(4566)
                .waitingFor(Wait.forLogMessage(/.*Ready[.].*/, 1))
    }

    def start() {
        container.start()
    }

    def stop() {
        container.stop()
    }

    String getEndpoint() {
        "http://localhost:${container.getMappedPort(4566)}"
    }
}