package com.example;

import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.VerifyDomainIdentityRequest;

import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MicronautEmailBugTest implements TestPropertyProvider {
    private static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.3.1"))
            .withServices(LocalStackContainer.Service.SES);

    static {
        localStack.start();
    }

    @Inject
    private SesClient sesClient;

    @Inject
    private EmbeddedApplication<?> application;

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
                "aws.secretKey", localStack.getSecretKey(),
                "aws.accessKeyId", localStack.getAccessKey(),
                "aws.services.ses.endpoint-override", localStack.getEndpointOverride(LocalStackContainer.Service.SES).toString()
        );
    }

    @BeforeEach
    public void setUp() throws Exception {
        sesClient.verifyDomainIdentity(VerifyDomainIdentityRequest.builder()
                .domain("foobar.com")
                .build());
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void foo(RequestSpecification spec) {
        spec
                .when()
                .contentType(ContentType.JSON)
                .post("/foo")
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.getCode());
    }

}
