package br.com.CIUBank.loan.service.user;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class PersonIdentificatorService {

	public PersonIdentificatorService(PersonRepository personRepository, PersonCNPJValidator cnpjValidator,
			PersonCPFValidator cpfValidator) {
		super();
		this.personRepository = personRepository;
		this.cnpjValidator = cnpjValidator;	
		this.cpfValidator = cpfValidator;
	}
	
	private final PersonRepository personRepository;
	private final PersonCNPJValidator cnpjValidator;
	private final PersonCPFValidator cpfValidator;

	final Map<String, BigDecimal> minimumParcelValues = Map.of("PF", BigDecimal.valueOf(300.00), "PJ",
			BigDecimal.valueOf(1000.00), "EU", BigDecimal.valueOf(100.00), "AP", BigDecimal.valueOf(400.00));

	final Map<String, BigDecimal> maximumLoanValues = Map.of("PF", BigDecimal.valueOf(10000.00), "PJ",
			BigDecimal.valueOf(100000.00), "EU", BigDecimal.valueOf(10000.00), "AP", BigDecimal.valueOf(25000.00));


	boolean isValidIdentifier(String identifier, String type) {
		return switch (type) {
		case "PF" -> cpfValidator.isValid(identifier);
		case "PJ" -> cnpjValidator.isValid(identifier);
		case "EU" -> isValidEUIdentifier(identifier);
		case "AP" -> isValidAPIdentifier(identifier);
		default -> false;
		};
	}

	private boolean isValidEUIdentifier(String identifier) {
	    return Optional.ofNullable(identifier)
	            .filter(id -> id.length() == 8)
	            .filter(id -> id.matches("\\d{8}"))
	            .map(id -> Character.getNumericValue(id.charAt(0)) + Character.getNumericValue(id.charAt(7)) == 9)
	            .orElse(false);
	}


	private boolean isValidAPIdentifier(String identifier) {
	    return Optional.ofNullable(identifier)
	            .filter(id -> id.length() == 10)
	            .filter(id -> id.substring(0, 9).chars().noneMatch(c -> c == id.charAt(9)))
	            .isPresent();
	}


	public String determineTypeIdentifier(final String identifier) {
	    return Optional.ofNullable(identifier)
	            .map(String::length)
	            .map(len -> switch (len) {
	                case 11 -> "PF";
	                case 14 -> "PJ";
	                case 8 -> "EU";
	                case 10 -> "AP";
	                default -> "Unknown";
	            })
	            .orElse("Unknown");
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
