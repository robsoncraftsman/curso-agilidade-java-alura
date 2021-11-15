package br.com.caelum.clines.api.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.caelum.clines.shared.domain.User;

public interface UserRepository extends Repository<User, Long> {

	void save(User user);
	List<User> findAll();
	Optional<User> findById(Long id);
	Optional<User> findByEmail(String email);

}
