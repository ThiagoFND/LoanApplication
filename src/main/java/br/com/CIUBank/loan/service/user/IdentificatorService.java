package br.com.CIUBank.loan.service.user;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class IdentificatorService {

	
	private final PersonRepository personRepository;
	private final CNPJValidator cnpjValidator;
	private final CPFValidator cpfValidator;

	private final Map<String, BigDecimal> minimumParcelValues = Map.of("PF", BigDecimal.valueOf(300.00), "PJ",
			BigDecimal.valueOf(1000.00), "EU", BigDecimal.valueOf(100.00), "AP", BigDecimal.valueOf(400.00));

	private final Map<String, BigDecimal> maximumLoanValues = Map.of("PF", BigDecimal.valueOf(10000.00), "PJ",
			BigDecimal.valueOf(100000.00), "EU", BigDecimal.valueOf(10000.00), "AP", BigDecimal.valueOf(25000.00));

	public IdentificatorService(PersonRepository personRepository, CNPJValidator cnpjValidator,
			CPFValidator cpfValidator) {
		super();
		this.personRepository = personRepository;
		this.cnpjValidator = cnpjValidator;
		this.cpfValidator = cpfValidator;
	}

	public boolean registerUser(final RegisterDTO data) {

		if (personRepository.findByLogin(data.login()) != null) {
			return false;
		}

		String typeIdentifier = determineTypeIdentifier(data.identifier());
		
		if (!isValidIdentifier(data.identifier(), typeIdentifier)) {
			return false;
		}

		BigDecimal valueMinimumParcels = minimumParcelValues.getOrDefault(typeIdentifier, BigDecimal.ZERO);
		BigDecimal valueMaximoLoan = maximumLoanValues.getOrDefault(typeIdentifier, BigDecimal.ZERO);

		if (valueMinimumParcels.compareTo(BigDecimal.ZERO) <= 0 || valueMaximoLoan.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}

		var encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		var newUser = new Person(data.name(), data.identifier(), data.birthDate(), typeIdentifier, valueMinimumParcels,
				valueMaximoLoan, data.active(), data.login(), encryptedPassword, data.role());

		personRepository.save(newUser);
		return true;
	}

	private boolean isValidIdentifier(String identifier, String type) {
		return switch (type) {
		case "PF" -> cpfValidator.isValid(identifier);
		case "PJ" -> cnpjValidator.isValid(identifier);
		case "EU" -> isValidEUIdentifier(identifier);
		case "AP" -> isValidAPIdentifier(identifier);
		default -> false;
		};
	}

	private boolean isValidEUIdentifier(String identifier) {
		if (identifier.length() != 8)
			return false;
		try {
			int firstDigit = Character.getNumericValue(identifier.charAt(0));
			int lastDigit = Character.getNumericValue(identifier.charAt(7));
			return firstDigit + lastDigit == 9;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isValidAPIdentifier(String identifier) {
		if (identifier.length() != 10)
			return false;
		char lastDigit = identifier.charAt(9);
		for (int i = 0; i < 9; i++) {
			if (identifier.charAt(i) == lastDigit)
				return false;
		}
		return true;
	}

	public String determineTypeIdentifier(final String identifier) {
		if (identifier == null) {
			return "Unknown";
		}
		return switch (identifier.length()) {
		case 11 -> "PF";
		case 14 -> "PJ";
		case 8 -> "EU";
		case 10 -> "AP";
		default -> "Unknown";
		};
	}

	public BigDecimal getMaxLoanValueForUser(String userId) {
		return personRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."))
				.getMaximumValueOfAllLoan();
	}
	
    public BigDecimal getMinInstallmentValueForUserType(String typeIdentifier) {
        return minimumParcelValues.getOrDefault(typeIdentifier, BigDecimal.ZERO);
    }
}
