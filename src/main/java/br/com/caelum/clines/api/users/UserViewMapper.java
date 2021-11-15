package br.com.caelum.clines.api.users;

import org.springframework.stereotype.Component;

import br.com.caelum.clines.shared.domain.User;
import br.com.caelum.clines.shared.infra.Mapper;

@Component
public class UserViewMapper implements Mapper<User, UserView> {

    @Override
	public UserView map(final User source) {
		return new UserView(source.getId(), source.getName(), source.getEmail());
    }
}
