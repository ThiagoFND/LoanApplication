package br.com.CIUBank.loan.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.dto.user.AuthenticationDTO;
import br.com.CIUBank.loan.dto.user.LoginResponseDTO;
import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.service.user.AuthorizationService;
import br.com.CIUBank.loan.service.user.PersonIdentificatorService;
import br.com.CIUBank.loan.service.user.PersonService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	private final AuthorizationService authorizationService;
	private final PersonService personService;

	public AuthenticationController(AuthorizationService authorizationService, PersonService personService) {
		super();
		this.authorizationService = authorizationService;
		this.personService = personService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
		String token = authorizationService.authenticate(data.login(), data.password());
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
		boolean registrationCompletedSuccessfully = personService.registerUser(data);
		if (!registrationCompletedSuccessfully) {
			return ResponseEntity.badRequest().body("Unable to complete registration. Check the data and try again.");
		}
		return ResponseEntity.ok().build();
	}
}
