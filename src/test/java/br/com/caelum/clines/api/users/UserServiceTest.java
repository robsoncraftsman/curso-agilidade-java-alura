package br.com.caelum.clines.api.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.caelum.clines.shared.domain.User;
import br.com.caelum.clines.shared.exceptions.ResourceAlreadyExistsException;
import br.com.caelum.clines.shared.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Spy
	private UserViewMapper viewMapper;

	@Spy
	private UserFormMapper formMapper;

	@Test
	public void shouldReturnUser() {
		final var expectedUserId = 1L;
		final var user = new User(expectedUserId, "Teste", "teste@email.com", "12345678");
		when(this.repository.findById(expectedUserId)).thenReturn(Optional.of(user));

		final var userView = this.service.showUserBy(expectedUserId);
		assertNotNull(userView);

		final var expectedView = new UserView(expectedUserId, user.getName(), user.getEmail());
		assertThat(userView, is(expectedView));
	}

	@Test
	public void shouldThrowExceptionIfUserDoesNotExists() {
		final long notFoundUserId = 1L;
		assertThrows(ResourceNotFoundException.class, () -> {
			this.service.showUserBy(notFoundUserId);
		});
	}

	@Test
	public void shouldCreateAUser() {
		final var userForm = new UserForm("Teste", "teste@email.com", "12345678");
		final var user = this.formMapper.map(userForm);
		given(this.formMapper.map(userForm)).willReturn(user);
		then(this.formMapper).should(only()).map(userForm);

		final var createdUserId = this.service.createUserBy(userForm);
		then(this.repository).should().save(user);
		assertEquals(user.getId(), createdUserId);
	}

	@Test
	public void shouldThrowResourceAlreadyExistsIfUserAlreadyExists() {
		final var user = new User("Teste", "teste@email.com", "12345678");
		given(this.repository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

		final var userForm = new UserForm(user.getName(), user.getEmail(), user.getPassword());
		assertThrows(ResourceAlreadyExistsException.class, () -> this.service.createUserBy(userForm));
	}

	@Test
	void listAllUsersShouldReturnAListWithAllUsers() {
		final User user1 = new User("Teste Um", "teste_um@email.com", "senha_um");
		final User user2 = new User("Teste Dois", "teste_dois@email.com", "senha_dois");
		final List<User> users = List.of(user1, user2);
		given(this.repository.findAll()).willReturn(users);

		final List<UserView> userViews = this.service.listAllUsers();
		then(this.repository).should(only()).findAll();
		then(this.viewMapper).should(times(2)).map(any(User.class));
		assertEquals(2, userViews.size());

		for (int i = 0; i < userViews.size(); i++) {
			assertEquals(users.get(i).getId(), userViews.get(i).getId());
			assertEquals(users.get(i).getName(), userViews.get(i).getName());
			assertEquals(users.get(i).getEmail(), userViews.get(i).getEmail());
		}
	}

	@Test
	void listAllUsersShouldReturnAnEmptyList() {
		given(this.repository.findAll()).willReturn(Collections.emptyList());

		final List<UserView> userViews = this.service.listAllUsers();

		then(this.repository).should(only()).findAll();
		then(this.viewMapper).should(times(0)).map(any(User.class));

		assertEquals(0, userViews.size());
	}

}
