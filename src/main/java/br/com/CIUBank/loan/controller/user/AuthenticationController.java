package br.com.CIUBank.loan.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.config.TokenService;
import br.com.CIUBank.loan.dto.user.AuthenticationDTO;
import br.com.CIUBank.loan.dto.user.LoginResponseDTO;
import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.repositories.PersonRepository;
import br.com.CIUBank.loan.service.user.IdentificadorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
	@Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PersonRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IdentificadorService identificadorService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Person) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        boolean registroSucesso = identificadorService.registrarUsuario(data);
        if (!registroSucesso) {
            return ResponseEntity.badRequest().body("Não foi possível completar o registro. Verifique os dados e tente novamente.");
        }
        return ResponseEntity.ok().build();
    }
}
