package br.com.CIUBank.loan.service.user;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.user.PersonDTO;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.mapper.DozerMapper;
import br.com.CIUBank.loan.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonService {

	private final PersonRepository personRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthorizationService authorizationService;
	private final Logger logger = Logger.getLogger(PersonService.class.getName());

	public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder,
			AuthorizationService authorizationService) {
		this.personRepository = personRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorizationService = authorizationService;
	}

	public List<PersonDTO> findAll(String id) {
		var users = personRepository.findAll();
		logger.info("Finding all users! Total found: " + users.size());
		return DozerMapper.parseListObjects(users, PersonDTO.class);
	}

	public Optional<PersonDTO> findById(String id) {
	    authorizeAccess(id);
	    return personRepository.findById(id)
	            .map(entity -> DozerMapper.parseObject(entity, PersonDTO.class));
	}



	@Transactional
	public PersonDTO updatePassword(String id, String oldPassword, String newPassword) {
		authorizeAccess(id);
		var user = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect.");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		personRepository.save(user);
		return DozerMapper.parseObject(user, PersonDTO.class);
	}

	private void authorizeAccess(String userId) {
		if (!authorizationService.isUserOwnerOfId(userId)) {
			logger.warning("Unauthorized access attempt for user ID: " + userId);
			throw new SecurityException("User is not authorized for this operation.");
		}
	}
	
    @Transactional
    public void deactivatePerson(String id) {
        authorizeAccess(id);
        var person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        if ("INACTIVE".equals(person.getActive())) {
            throw new IllegalStateException("Person is already deactivated.");
        }

        person.setActive("INACTIVE");
        personRepository.save(person);
        logger.info("Person with ID: " + id + " has been deactivated.");
    }
}
