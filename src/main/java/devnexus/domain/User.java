package devnexus.domain;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;

import static io.micronaut.data.annotation.GeneratedValue.Type.AUTO;

@MappedEntity("users")
public class User {

    @Id
    @GeneratedValue(AUTO)
    private Long id;

    @NotNull
    private final String username;

    @NotNull
    private final String password;

    private final boolean admin;

    @Creator
    public User(Long id, String username, String password, boolean admin) {
        this(username, password, admin);
        this.id = id;
    }

    public User(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public User(String username, String password) {
        this(username, password, false);
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }
}
