package devnexus.controller;

import devnexus.domain.Genre;
import devnexus.repository.GenreRepository;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static io.micronaut.http.HttpHeaders.LOCATION;
import static io.micronaut.http.HttpStatus.NO_CONTENT;

@ExecuteOn(TaskExecutors.IO)
@Controller("/genres")
class GenreController {

    private final GenreRepository genreRepository;

    GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
    public Optional<Genre> show(long id) {
        return genreRepository.findById(id);
    }

    @Put
    public HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) {
        genreRepository.update(command.getId(), command.getName());
        return HttpResponse
                .noContent()
                .header(LOCATION, location(command.getId()).getPath());
    }

    @Get("/list")
    List<Genre> list(@Valid Pageable pageable) {
        return genreRepository.findAll(pageable).getContent();
    }

    @Post
    HttpResponse<Genre> save(@Body("name") @NotBlank String name) {
        Genre genre = genreRepository.save(name);

        return HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId())));
    }

    @Post("/ex")
    HttpResponse<Genre> saveExceptions(@Body @NotBlank String name) {
        try {
            Genre genre = genreRepository.saveWithException(name);
            return HttpResponse
                    .created(genre)
                    .headers(headers -> headers.location(location(genre.getId())));
        } catch(DataAccessException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}")
    @Status(NO_CONTENT)
    void delete(long id) {
        genreRepository.deleteById(id);
    }

    private URI location(Long id) {
        return URI.create("/genres/" + id);
    }
}
