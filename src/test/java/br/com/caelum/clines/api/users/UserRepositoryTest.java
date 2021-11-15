package br.com.caelum.clines.api.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.caelum.clines.shared.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
	private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
	void shouldReturnUserByIdWhenExistsAnUserInDB() {
		final var newUser = new User("Teste", "teste@email.com", "12345678");
		final var persistedUser = this.entityManager.persist(newUser);

		final var optionalUserFromDb = this.repository.findById(persistedUser.getId());
		assertTrue(optionalUserFromDb.isPresent());

		final var userFromDb = optionalUserFromDb.get();
		assertEquals(persistedUser.getId(), userFromDb.getId());
		assertEquals(newUser.getName(), userFromDb.getName());
		assertEquals(newUser.getEmail(), userFromDb.getEmail());
		assertEquals(newUser.getPassword(), userFromDb.getPassword());
    }

	@Test
	void shouldReturnUserByEmailWhenExistsAnUserInDB() {
		final var newUser = new User("Teste", "teste@email.com", "12345678");
		final var persistedUser = this.entityManager.persist(newUser);

		final var optionalUserFromDb = this.repository.findByEmail(persistedUser.getEmail());
		assertTrue(optionalUserFromDb.isPresent());

		final var userFromDb = optionalUserFromDb.get();
		assertEquals(persistedUser.getId(), userFromDb.getId());
		assertEquals(newUser.getName(), userFromDb.getName());
		assertEquals(newUser.getEmail(), userFromDb.getEmail());
		assertEquals(newUser.getPassword(), userFromDb.getPassword());
	}

    @Test
	void shouldReturnAnEmptyOptionalWhenNotExistsUserByCode() {
		final var optionalUserFromDb = this.repository.findById(999L);
		assertTrue(optionalUserFromDb.isEmpty());
    }

    @Test
	void shouldSaveANewUser() {
		final var newUser = new User("New", "new@email.com", "12345678");
		this.repository.save(newUser);

		final Optional<User> optionalUserFromDb = this.repository.findById(newUser.getId());
		assertTrue(optionalUserFromDb.isPresent());

		final var persistedUser = optionalUserFromDb.get();
		assertEquals(newUser.getName(), persistedUser.getName());
		assertEquals(newUser.getEmail(), persistedUser.getEmail());
		assertEquals(newUser.getPassword(), persistedUser.getPassword());
    }

}