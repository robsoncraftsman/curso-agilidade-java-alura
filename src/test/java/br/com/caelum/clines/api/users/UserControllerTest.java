package br.com.caelum.clines.api.users;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.caelum.clines.shared.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    @Test
	void shouldReturn404WhenNotExistUserByCode() throws Exception {
		this.mockMvc.perform(get("/user/999")).andExpect(status().isNotFound());
    }

    @Test
	void shouldReturnAnUserByCode() throws Exception {
		final var user = new User("Teste", "teste@email.com", "12345678");
		final var persistedUser = this.entityManager.persist(user);
		final long id = persistedUser.getId();

		this.mockMvc.perform(get("/user/" + id))
                .andExpect(status().isOk())
                .andDo(log())
				.andExpect(jsonPath("$.id", equalTo(Long.valueOf(id).intValue())))
				.andExpect(jsonPath("$.name", equalTo(user.getName())))
				.andExpect(jsonPath("$.email", equalTo(user.getEmail())));
    }

	@Test
	void shouldCreateNewUser() throws Exception {
		final var json = "{\"name\":\"New user\", \"email\":\"newuser@email.com\", \"password\":\"12345678\"}";
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated())
				.andDo(log());
	}

	@Test
	void shouldNotCreateNewUserWhenAnotherUserWithSameEmailAlreadyExists() throws Exception {
		final var jsonAnotherUser = "{\"name\":\"Another user\", \"email\":\"user@email.com\", \"password\":\"12345678\"}";
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(jsonAnotherUser))
				.andExpect(status().isCreated())
				.andDo(log());

		final var jsonNewUser = "{\"name\":\"New user\", \"email\":\"user@email.com\", \"password\":\"12345678\"}";
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(jsonNewUser))
				.andExpect(status().isConflict())
				.andDo(log())
				.andExpect(jsonPath("$.errors[0].message", equalTo("User already exists")));
	}

	@Test
	void shouldReturnAllUsers() throws Exception {
		final var userOne = new User("User One", "userone@email.com", "12345678");
		this.entityManager.persist(userOne);
		final var userTwo = new User("User Two", "usertwo@email.com", "12345678");
		this.entityManager.persist(userTwo);

		this.mockMvc.perform(get("/user"))
				.andExpect(status().isOk())
				.andDo(log())
				.andExpect(jsonPath("$[0].name", equalTo(userOne.getName())))
				.andExpect(jsonPath("$[0].email", equalTo(userOne.getEmail())))
				.andExpect(jsonPath("$[1].name", equalTo(userTwo.getName())))
				.andExpect(jsonPath("$[1].email", equalTo(userTwo.getEmail())));
	}

}