package br.com.CIUBank.loan.controller.user;
	
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.config.TokenService;
import br.com.CIUBank.loan.dto.user.AuthenticationDTO;
import br.com.CIUBank.loan.dto.user.LoginResponseDTO;
import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.service.user.IdentificatorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final IdentificatorService identificatorService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, IdentificatorService identificatorService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.identificatorService = identificatorService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Person) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        boolean registroSucesso = identificatorService.registerUser(data);
        if (!registroSucesso) {
            return ResponseEntity.badRequest().body("Unable to complete registration. Check the data and try again.");
        }
        return ResponseEntity.ok().build();
    }
}
