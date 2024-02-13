package br.com.CIUBank.loan.controller.user;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody; // Importação correta
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.dto.user.PasswordUpdateDTO;
import br.com.CIUBank.loan.dto.user.PersonDTO;
import br.com.CIUBank.loan.service.user.PersonService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class PersonController {

	@Autowired
	private PersonService personService;

	private Logger logger = Logger.getLogger(PersonController.class.getName());

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PatchMapping("/changePassword/{id}")
	public ResponseEntity<?> updatePassword(@PathVariable String id,
			@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {

		personService.updatePassword(id, passwordUpdateDTO.getOldPassword(), passwordUpdateDTO.getNewPassword());
		return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
	}

	@GetMapping("/{id}")
	public ResponseEntity<PersonDTO> findById(@PathVariable String id) {
	    logger.info("Request received to find user with ID: " + id);
	    return personService.findById(id)
	            .map(personDTO -> {
	                logger.info("User found with ID: " + id);
	                return new ResponseEntity<>(personDTO, HttpStatus.OK);
	            })
	            .orElseGet(() -> {
	                logger.severe("No user found with ID: " + id);
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            });
	}


	@DeleteMapping("/deactivate/{id}")
	public ResponseEntity<?> deactivatePerson(@PathVariable String id) {
		try {
			logger.info("Request received to deactivate user with ID: " + id);
			personService.deactivatePerson(id);
			return new ResponseEntity<>("User deactivated successfully.", HttpStatus.OK);
		} catch (Exception e) {
			logger.severe("Error deactivating user with ID: " + id + ". Error: " + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
