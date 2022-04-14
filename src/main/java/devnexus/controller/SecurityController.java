package devnexus.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.views.View;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

@Secured(IS_ANONYMOUS)
@Controller
class SecurityController {

    @Get
    @View("home")
    Map<String, Object> index(@Nullable Principal principal) {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedIn", principal != null);
        if (principal != null) {
            model.put("username", principal.getName());
        }
        return model;
    }

    @Get("/login/auth")
    @View("auth")
    Map<String, Object> auth() {
        return new HashMap<>();
    }

    @Get("/login/authFailed")
    @View("auth")
    Map<String, Object> authFailed() {
        Map<String, Object> model = new HashMap<>();
        model.put("errors", true);
        return model;
    }

    @Secured(IS_AUTHENTICATED)
    @Get(value = "/user", produces = TEXT_PLAIN)
    String anyUser(Principal principal) {
        return "Hello user '" + principal.getName() + "'";
    }

    @Secured("ROLE_ADMIN")
    @Get(value = "/admin", produces = TEXT_PLAIN)
    String admin(Principal principal) {
        return "Hello admin '" + principal.getName() + "', the time is " + new Date();
    }
}
