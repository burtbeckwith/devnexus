package devnexus.security;

import devnexus.domain.User;
import devnexus.repository.UserRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Singleton
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final BCryptEncoder passwordEncoder;

    DatabaseAuthenticationProvider(UserRepository userRepository,
                                   BCryptEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authRequest) {
        return Flux.create(emitter -> {

            String username = (String) authRequest.getIdentity();
            String password = (String) authRequest.getSecret();
            Optional<User> user = authenticate(username, password);

            if (user.isPresent()) {
                emitter.next(AuthenticationResponse.success(username, determineRoles(user.get())));
                emitter.complete();
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }

    private Optional<User> authenticate(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            return Optional.empty();
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            return Optional.empty();
        }

        return user;
    }

    private Collection<String> determineRoles(User user) {
        return user.isAdmin() ? Arrays.asList(ROLE_USER, ROLE_ADMIN) : Collections.singleton(ROLE_USER);
    }
}
