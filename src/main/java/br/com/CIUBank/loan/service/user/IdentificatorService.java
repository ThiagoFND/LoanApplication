package br.com.CIUBank.loan.service.user;

import java.math.BigDecimal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class IdentificatorService {

	private PersonRepository personRepository;

    public IdentificatorService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public boolean registerUser(RegisterDTO data) {
        if (personRepository.findByLogin(data.login()) != null) {
            return false;
        }

        String typeIdentifier = determineTypeIdentifier(data.identifier());

        if ("Unknown".equals(typeIdentifier)) {
            return false;
        }

        BigDecimal valueMinimumParcels = calculateMinimumValueParcels(typeIdentifier);
        BigDecimal valueMaximoLoan = calculateMaximumValueLoan(typeIdentifier);

        if (valueMinimumParcels.compareTo(BigDecimal.ZERO) <= 0 || valueMaximoLoan.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Person newUser = new Person(data.name(), data.identifier(), data.birthDate(), typeIdentifier,
                valueMinimumParcels, valueMaximoLoan, data.login(), encryptedPassword, data.role());

        personRepository.save(newUser);
        return true;
    }


	public String determineTypeIdentifier(String identifier) {
		if (identifier == null) {
			return null;
		}
		switch (identifier.length()) {
		case 11:
			return "PF";
		case 14:
			return "PJ";
		case 8:
			return "EU";
		case 10:
			return "AP";
		default:
			return "Unknown";
		}
	}

	public BigDecimal calculateMinimumValueParcels(String typeIdentifier) {
		switch (typeIdentifier) {
		case "PF":
			return BigDecimal.valueOf(300.00);
		case "PJ":
			return BigDecimal.valueOf(1000.00);
		case "EU":
			return BigDecimal.valueOf(100.00);
		case "AP":
			return BigDecimal.valueOf(400.00);
		default:
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal calculateMaximumValueLoan(String typeIdentifier) {
		switch (typeIdentifier) {
		case "PF":
			return BigDecimal.valueOf(10000.00);
		case "PJ":
			return BigDecimal.valueOf(100000.00);
		case "EU":
			return BigDecimal.valueOf(10000.00);
		case "AP":
			return BigDecimal.valueOf(25000.00);
		default:
			return BigDecimal.ZERO;
		}
	}

	   public BigDecimal getMaxLoanValueForUser(String userId) {
	        return personRepository.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."))
	                .getMaximumValueOfAllLoan();
	    }

}
