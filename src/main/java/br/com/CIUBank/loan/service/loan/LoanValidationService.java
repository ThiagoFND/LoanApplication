package br.com.CIUBank.loan.service.loan;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.exceptions.BusinessException;
import br.com.CIUBank.loan.service.user.IdentificatorService;

@Service
public class LoanValidationService {

	private final IdentificatorService identificatorService;
	private final LoanRepositoryWrapper loanRepositoryWrapper;
	private final Logger logger = Logger.getLogger(LoanValidationService.class.getName());

	public LoanValidationService(IdentificatorService identificatorService,
			LoanRepositoryWrapper loanRepositoryWrapper) {
		super();
		this.identificatorService = identificatorService;
		this.loanRepositoryWrapper = loanRepositoryWrapper;
	}

	public void validateLoanCreation(LoanDTO loanDTO) {
		validateLoanDTO(loanDTO);
		validateNumberOfInstallments(loanDTO);
	}

	public void validateLoanAmountAgainstMaxValue(BigDecimal loanAmount, String userId) {
		BigDecimal maxLoanValue = identificatorService.getMaxLoanValueForUser(userId);
		if (loanAmount.compareTo(maxLoanValue) > 0) {
			logger.warning("Attempt to exceed max loan value for user ID: " + userId);
			throw new BusinessException("The loan amount exceeds the maximum allowed value for this user.");
		}
	}

	public void validateInstallmentValue(LoanDTO loanDTO, String userTypeIdentifier, BigDecimal parcelValue) {
		BigDecimal minParcelValue = identificatorService.getMinInstallmentValueForUserType(userTypeIdentifier);
		if (parcelValue.compareTo(minParcelValue) < 0) {
			throw new BusinessException("The installment value is less than the minimum allowed for this user type.");
		}
	}

	private void validateLoanDTO(LoanDTO loanDTO) {
		if (loanDTO.getPerson() == null || loanDTO.getPerson().getId() == null) {
			throw new IllegalArgumentException("Person ID is required in the loan DTO.");
		}
	}

	private void validateNumberOfInstallments(LoanDTO loanDTO) {
		if (loanDTO.getNumberParcels() > 24) {
			logger.warning("Attempt to create a loan with more than 24 installments for user ID: "
					+ loanDTO.getPerson().getId());
			throw new BusinessException("Loan cannot have more than 24 installments.");
		}
	}
	
	public void validateLoanStatusBeforePayment(LoanDTO loanDTO) {
	    if ("PAID".equalsIgnoreCase(loanDTO.getStatusPayment())) {
	        throw new BusinessException("This loan is already marked as PAID.");
	    }
	}
	
	public void validateLoanStatusBeforeDeactivation(LoanDTO loanDTO) {
	    if ("INACTIVE".equalsIgnoreCase(loanDTO.getStatusPayment())) {
	        throw new BusinessException("This loan is already marked as INACTIVE.");
	    }
	}

}