package br.com.CIUBank.loan.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.CIUBank.loan.entity.user.PersonRole;

public record RegisterDTO(String name, String identifier, LocalDate birthDate,  String typeIdentifier, BigDecimal minimumMonthlyValueOfInstallments,
		BigDecimal maximumValueOfAllLoan, String login, String password, PersonRole role) {
}