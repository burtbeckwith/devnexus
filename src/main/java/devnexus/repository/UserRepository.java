package devnexus.repository;

import devnexus.domain.User;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static io.micronaut.data.model.query.builder.sql.Dialect.MYSQL;

@JdbcRepository(dialect = MYSQL)
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(@NotNull String username);
}
