package br.com.CIUBank.loan.service.user;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.config.SecurityConfigurations;
import br.com.CIUBank.loan.dto.user.PersonDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.mapper.DozerMapper;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository personReposiory;
	@Autowired
	private SecurityConfigurations securityConfigurations;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthorizationService authorizationService;

	private Logger logger = Logger.getLogger(PersonDTO.class.getName());

	public List<PersonDTO> findAll() {

		logger.info("Finding all users!");

		return DozerMapper.parseListObjects(personReposiory.findAll(), PersonDTO.class);
	}

	public PersonDTO findById(String id) {

		logger.info("Finding one users!");

		var entity = personReposiory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return DozerMapper.parseObject(entity, PersonDTO.class);
	}

	public void delete(String id) {

		logger.info("Deleting one user!");

		var entity = personReposiory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		personReposiory.delete(entity);
	}

	public PersonDTO updatePassword(String id, String oldPassword, String newPassword) {
		logger.info("Updating user password!");

		// Primeiro, verifica se o usuário autenticado é o dono do ID
		if (!authorizationService.isUserOwnerOfId(id)) {
			throw new SecurityException("User is not authorized to update password for this ID.");
		}

		Person user = personReposiory.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		// Verifies if the old password matches
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect.");
		}

		// Encode and updates to the new password
		user.setPassword(passwordEncoder.encode(newPassword));
		personReposiory.save(user);

		return DozerMapper.parseObject(user, PersonDTO.class);
	}

}
