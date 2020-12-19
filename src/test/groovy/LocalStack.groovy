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
                .withEnv(
                        SERVICES: 's3,iam,lambda',
                        DEFAULT_REGION: region,
                        LAMBDA_EXECUTOR: 'docker',
                        LAMBDA_REMOTE_DOCKER: 'true') // https://github.com/localstack/localstack/issues/3185
                .withExposedPorts(4566)
                .waitingFor(forLogMessage(/.*Ready[.].*/, 1))
        container.withFileSystemBind('//var/run/docker.sock', '/var/run/docker.sock')
    }

    def start() {
        container.start()
    }

    def stop() {
        container.stop()
    }

    URI getEndpoint() {
        "http://localhost:${container.getMappedPort(4566)}".toURI()
    }
}