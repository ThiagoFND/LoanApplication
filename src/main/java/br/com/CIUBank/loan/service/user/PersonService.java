package br.com.CIUBank.loan.service.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.user.PersonDTO;
import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.mapper.DozerMapper;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;
    public final PersonIdentificatorService identificatorService;
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder,
			AuthorizationService authorizationService, PersonIdentificatorService identificatorService) {
		super();
		this.personRepository = personRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorizationService = authorizationService;
		this.identificatorService = identificatorService;
	}

	public List<PersonDTO> findAll() {
        var users = personRepository.findAll();
        logger.info("Finding all users! Total found: " + users.size());
        return DozerMapper.parseListObjects(users, PersonDTO.class);
    }
    
	public boolean registerUser(final RegisterDTO data) {
	    boolean userExists = personRepository.findByIdentifier(data.identifier()).isPresent();
	    if (userExists) {
	        return false;
	    }

		return Optional.ofNullable(identificatorService.determineTypeIdentifier(data.identifier()))
				.filter(typeIdentifier -> identificatorService.isValidIdentifier(data.identifier(), typeIdentifier))
				.map(typeIdentifier -> {
					BigDecimal valueMinimumParcels = identificatorService.minimumParcelValues
							.getOrDefault(typeIdentifier, BigDecimal.ZERO);
					BigDecimal valueMaximoLoan = identificatorService.maximumLoanValues.getOrDefault(typeIdentifier,
							BigDecimal.ZERO);
					return new BigDecimal[] { valueMinimumParcels, valueMaximoLoan };
				})
				.filter(values -> values[0].compareTo(BigDecimal.ZERO) > 0 && values[1].compareTo(BigDecimal.ZERO) > 0)
				.map(values -> {
					var encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
					var newUser = new Person(data.name(), data.identifier(), data.birthDate(),
							identificatorService.determineTypeIdentifier(data.identifier()), values[0], values[1],
							data.active(), data.login(), encryptedPassword, data.role());
					personRepository.save(newUser);
					return true;
				}).orElse(false);
	}

    public Optional<PersonDTO> findById(String id) {
        authorizeAccess(id);
        return personRepository.findById(id).map(entity -> DozerMapper.parseObject(entity, PersonDTO.class));
    }

    public PersonDTO updatePassword(String id, String oldPassword, String newPassword) {
        authorizeAccess(id);
        var user = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        validateOldPassword(oldPassword, user.getPassword());

        user.setPassword(passwordEncoder.encode(newPassword));
        personRepository.save(user);
        return DozerMapper.parseObject(user, PersonDTO.class);
    }

    public void deactivatePerson(String id) {
        authorizeAccess(id);
        var person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        deactivateIfActive(person);
    }
    
    private void validateOldPassword(String oldPassword, String currentPassword) {
        Optional.of(oldPassword)
                .filter(pass -> passwordEncoder.matches(pass, currentPassword))
                .orElseThrow(() -> new IllegalArgumentException("Old password is incorrect."));
    }

    private void deactivateIfActive(Person person) {
        Optional.of(person)
            .filter(p -> !"INACTIVE".equals(p.getActive()))
            .ifPresentOrElse(p -> {
                p.setActive("INACTIVE");
                personRepository.save(p);
                logger.info("Person with ID: " + p.getId() + " has been deactivated.");
            }, () -> {
                throw new IllegalStateException("Person is already deactivated.");
            });
    }
    
    private void authorizeAccess(String userId) {
        authorizationService.authorizeAccess(userId);
    }
    
    public boolean existsByIdentifier(String identifier) {
        return personRepository.findByIdentifier(identifier).isPresent();
    }

}
