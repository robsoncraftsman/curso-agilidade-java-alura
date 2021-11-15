package br.com.caelum.clines.api.users;

import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    ResponseEntity<?> createBy(@RequestBody @Valid final UserForm form) {
		final var id = this.service.createUserBy(form);
		final var uri = URI.create("/user/").resolve(String.valueOf(id));
        return created(uri).build();
    }

    @GetMapping
    List<UserView> list() {
        return this.service.listAllUsers();
    }

    @GetMapping("{id}")
    UserView show(@PathVariable final Long id) {
        return this.service.showUserBy(id);
    }
}
