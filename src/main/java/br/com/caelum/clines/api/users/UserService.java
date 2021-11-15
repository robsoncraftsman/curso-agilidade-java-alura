package br.com.caelum.clines.api.users;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.caelum.clines.shared.exceptions.ResourceAlreadyExistsException;
import br.com.caelum.clines.shared.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	private final UserRepository repository;
	private final UserFormMapper formMapper;
	private final UserViewMapper viewMapper;

	public Long createUserBy(final UserForm form) {
		this.repository.findByEmail(form.getEmail()).ifPresent(existingLocation -> {
			throw new ResourceAlreadyExistsException("User already exists");
		});

		final var user = this.formMapper.map(form);
		this.repository.save(user);
		return user.getId();
    }

	public List<UserView> listAllUsers() {
        return this.repository.findAll().stream().map(this.viewMapper::map).collect(toList());
    }

	public UserView showUserBy(final Long UserId) {
		final var User = this.repository.findById(UserId)
				.orElseThrow(() -> new ResourceNotFoundException("Cannot find user"));
		return this.viewMapper.map(User);
    }
}
