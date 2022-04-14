package devnexus.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;

@Secured(IS_ANONYMOUS)
@Controller("/hello")
class HelloController {

    @Get(produces = TEXT_PLAIN)
    String index() {
        return "Hello World";
    }
}
