import org.testcontainers.containers.GenericContainer

import static org.testcontainers.containers.wait.strategy.Wait.forLogMessage
import static org.testcontainers.images.PullPolicy.alwaysPull
import static org.testcontainers.images.PullPolicy.defaultPolicy

class LocalStack {
    final GenericContainer container
    final accessKey = 'foo'
    final secretKey = 'bar'
    final region = 'eu-west-1'

    LocalStack(version) {
        container = new GenericContainer("localstack/localstack:$version")
                .withImagePullPolicy(version == 'latest' ? alwaysPull() : defaultPolicy())
                .withEnv(SERVICES: 's3', DEFAULT_REGION: region)
                .withExposedPorts(4566)
                .waitingFor(forLogMessage(/.*Ready[.].*/, 1))
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