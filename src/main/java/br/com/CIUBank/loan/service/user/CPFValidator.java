package br.com.CIUBank.loan.service.user;

import org.springframework.stereotype.Component;

@Component
public class CPFValidator implements IdentifierValidator {
	@Override
	public boolean isValid(String cpfInput) {
		var cpf = cpfInput.replaceAll("\\D+", "");

		if (cpf.length() != 11 || cpf.chars().allMatch(c -> c == cpf.charAt(0))) {
			return false;
		}

		for (int i = 9; i < 11; i++) {
			int sum = 0;
			for (int j = 0; j < i; j++) {
				sum += (cpf.charAt(j) - '0') * (i + 1 - j);
			}
			sum = 11 - (sum % 11);
			if (sum > 9) {
				sum = 0;
			}
			if (sum != (cpf.charAt(i) - '0')) {
				return false;
			}
		}
		return true;
	}
}
