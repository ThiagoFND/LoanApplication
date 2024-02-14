package br.com.CIUBank.loan.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CNPJValidator implements IdentifierValidator {

	private static final Logger logger = LoggerFactory.getLogger(CNPJValidator.class);

	@Override
	public boolean isValid(String cnpjInput) {
	    var cnpj = cnpjInput.replaceAll("\\D+", "");

	    if (cnpj.length() != 14 || cnpj.chars().allMatch(c -> c == cnpj.charAt(0))) {
	        return false;
	    }

	    int[] weightCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	    for (int t = 12; t < 14; t++) {
	        int sum = 0;
	        for (int i = 0; i < t; i++) {
	            sum += (cnpj.charAt(i) - '0') * weightCNPJ[i + (weightCNPJ.length - t)];
	        }
	        int digit = 11 - (sum % 11);
	        digit = digit >= 10 ? 0 : digit;
	        if (digit != (cnpj.charAt(t) - '0')) {
	            return false;
	        }
	    }
	    return true;
	}

}
