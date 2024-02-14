package br.com.CIUBank.loan.service.user;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.user.PersonDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.mapper.DozerMapper;
import br.com.CIUBank.loan.repositories.PersonRepository;

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

    public List<PersonDTO> findAll() {
        var users = personRepository.findAll();
        logger.info("Finding all users! Total found: " + users.size());
        return DozerMapper.parseListObjects(users, PersonDTO.class);
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
        if (!passwordEncoder.matches(oldPassword, currentPassword)) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
    }

    private void deactivateIfActive(Person person) {
        if (!"INACTIVE".equals(person.getActive())) {
            person.setActive("INACTIVE");
            personRepository.save(person);
            logger.info("Person with ID: " + person.getId() + " has been deactivated.");
        } else {
            throw new IllegalStateException("Person is already deactivated.");
        }
    }
    
    private void authorizeAccess(String userId) {
        authorizationService.authorizeAccess(userId);
    }
}
