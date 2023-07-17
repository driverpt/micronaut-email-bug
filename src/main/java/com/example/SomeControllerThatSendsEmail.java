package com.example;

import io.micronaut.email.Email;
import io.micronaut.email.EmailSender;
import io.micronaut.email.template.TemplateBody;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller("/foo")
@Singleton
public class SomeControllerThatSendsEmail {
    private final EmailSender<?, ?> emailSender;

    public SomeControllerThatSendsEmail(final EmailSender<?, ?> emailSender) {
        this.emailSender = emailSender;
    }


    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public Mono<? extends HttpResponse<?>> foo() {
        return Mono.fromCallable(() -> emailSender.send(Email.builder()
                        .from("foo@foobar.com")
                        .to("baz@foobar.com")
                        .subject("Some Email")
                        .body(new TemplateBody<>("foo", Map.of("name", "bar")))))
                .map(ignored -> HttpResponse.status(HttpStatus.NO_CONTENT));
    }

}
