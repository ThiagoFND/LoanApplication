package br.com.CIUBank.loan.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class AuthorizationService implements UserDetailsService {

	@Autowired
	PersonRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByLogin(username);
	}
	

    public boolean isUserOwnerOfId(String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Person) {
                Person loggedInUser = (Person) principal;
                return userId.equals(loggedInUser.getId());
            }
        }
        return false;
    }
}
