package devnexus.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import static io.micronaut.http.MediaType.TEXT_PLAIN;

@Controller("/hello")
class HelloController {

    @Get(produces = TEXT_PLAIN)
    String index() {
        return "Hello World";
    }
}
