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

	public IdentificatorService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	private final Map<String, BigDecimal> minimumParcelValues = Map.of("PF", BigDecimal.valueOf(300.00), "PJ",
			BigDecimal.valueOf(1000.00), "EU", BigDecimal.valueOf(100.00), "AP", BigDecimal.valueOf(400.00));

	private final Map<String, BigDecimal> maximumLoanValues = Map.of("PF", BigDecimal.valueOf(10000.00), "PJ",
			BigDecimal.valueOf(100000.00), "EU", BigDecimal.valueOf(10000.00), "AP", BigDecimal.valueOf(25000.00));

	public boolean registerUser(final RegisterDTO data) {
		if (personRepository.findByLogin(data.login()) != null) {
			return false;
		}

		String typeIdentifier = determineTypeIdentifier(data.identifier());
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
}
