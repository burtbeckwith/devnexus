package devnexus.security;

import devnexus.domain.User;
import devnexus.repository.UserRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Arrays;

import static io.micronaut.context.env.Environment.TEST;

@Singleton
@Requires(notEnv = TEST)
class DataPopulator {

    private final UserRepository userRepository;
    private final BCryptEncoder passwordEncoder;

    DataPopulator(UserRepository userRepository,
                  BCryptEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    @Transactional
    void init(StartupEvent event) {
        // clear out any existing users
        userRepository.deleteAll();

        // create data
        User user = new User("some_user", passwordEncoder.encode("password1"));
        User admin = new User("an_admin", passwordEncoder.encode("password2"), true);
        userRepository.saveAll(Arrays.asList(user, admin));
    }
}
