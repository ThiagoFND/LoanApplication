package br.com.CIUBank.loan.service.user;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.config.TokenService;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    private final AuthenticationManager authenticationManager;
    private final PersonRepository repository;
    private final TokenService tokenService;

    public AuthorizationService(@Lazy AuthenticationManager authenticationManager, PersonRepository repository,
                                TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public boolean isUserOwnerOfId(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();

            if (principal instanceof Person) {
                var loggedInUser = (Person) principal;
                return userId.equals(loggedInUser.getId());
            }
        }
        return false;
    }

    public String authenticate(String username, String password) {
        var person = (Person) repository.findByLogin(username);
        if (person == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        checkIfUserIsActive(person.getId());

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (!(authentication.getPrincipal() instanceof Person)) {
            throw new ClassCastException("Unable to cast authentication principal to Person");
        }
        person = (Person) authentication.getPrincipal();
        
        return tokenService.generateToken(person);
    }

    public void checkIfUserIsActive(String id) {
        var person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        if ("INACTIVE".equals(person.getActive())) {
            throw new SecurityException("User is inactive and cannot perform this operation.");
        }
    }
}
