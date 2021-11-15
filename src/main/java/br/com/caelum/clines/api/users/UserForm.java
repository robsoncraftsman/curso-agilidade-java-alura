package br.com.caelum.clines.api.users;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserForm {

	@NotNull
    private String name;

	@NotNull
	private String email;

	@NotNull
	private String password;

}
