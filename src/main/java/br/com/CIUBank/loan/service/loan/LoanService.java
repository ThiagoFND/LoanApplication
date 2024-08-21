package br.com.CIUBank.loan.service.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.entity.loan.Loan;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.BusinessException;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.service.user.AuthorizationService;

@Service
public class LoanService {

	private final LoanRepositoryWrapper loanRepositoryWrapper;
	private final LoanValidationService loanValidationService;
	private final LoanAuthorizationService loanAuthorizationService;
	private final LoanMapperService loanMapperService;
	private final AuthorizationService authorizationService;

	public LoanService(LoanRepositoryWrapper loanRepositoryWrapper, LoanValidationService loanValidationService,
			LoanAuthorizationService loanAuthorizationService, LoanMapperService loanMapperService,
			AuthorizationService authorizationService) {
		super();
		this.loanRepositoryWrapper = loanRepositoryWrapper;
		this.loanValidationService = loanValidationService;
		this.loanAuthorizationService = loanAuthorizationService;
		this.loanMapperService = loanMapperService;
		this.authorizationService = authorizationService;
	}

	public List<LoanDTO> findAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String userId = ((Person) authentication.getPrincipal()).getId();
			List<Loan> userLoans = loanRepositoryWrapper.findByPersonId(userId);
			return loanMapperService.toLoanDTOList(userLoans);
		} else {
			throw new BusinessException("User is not authenticated.");
		}
	}

	public Optional<LoanDTO> findById(String id) {
		Loan loan = loanRepositoryWrapper.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan not found for the given ID."));
		loanAuthorizationService.authorizeAccessForUser(loan.getPerson().getId().toString());
		return Optional.of(loanMapperService.toLoanDTO(loan));
	}

	public LoanDTO create(LoanDTO loanDTO) {
		verifyOwnershipForLoan(loanDTO.getPerson().getId());
		
		loanValidationService.validateLoanCreation(loanDTO);

		Person user = loanRepositoryWrapper.findPersonById(loanDTO.getPerson().getId());

		BigDecimal installmentValue = loanDTO.getValueLoan().divide(BigDecimal.valueOf(loanDTO.getNumberParcels()),
				RoundingMode.HALF_UP);
		loanValidationService.validateLoanAmountAgainstMaxValue(loanDTO.getValueLoan(), user.getId());
		loanValidationService.validateInstallmentValue(loanDTO, user.getTypeIdentifier(), installmentValue);

		Loan loan = loanMapperService.toLoanEntity(loanDTO);
		loan.setPerson(user);
		Loan savedLoan = loanRepositoryWrapper.saveLoan(loan);

		return loanMapperService.toLoanDTO(savedLoan);
	}

	public void markLoanAsInactive(String id) {
	    Loan loan = loanRepositoryWrapper.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	    loanAuthorizationService.authorizeAccessForUser(loan.getPerson().getId().toString());

	    LoanDTO loanDTO = loanMapperService.toLoanDTO(loan);
	    loanValidationService.validateLoanStatusBeforeDeactivation(loanDTO);

	    loanDTO.setStatusPayment("INACTIVE");
	    Loan updatedLoan = loanMapperService.toLoanEntity(loanDTO);
	    loanRepositoryWrapper.saveLoan(updatedLoan);
	}


	
	private void verifyOwnershipForLoan(String userId) {
	    if (!authorizationService.isUserOwnerOfId(userId)) {
	        throw new BusinessException("User is not authorized to perform this operation.");
	    }
	}
	
	public void markLoanAsPaid(String id) {
	    Loan loan = loanRepositoryWrapper.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Loan not found for the given ID."));
	    loanAuthorizationService.authorizeAccessForUser(loan.getPerson().getId().toString());
	    
	    LoanDTO loanDTO = loanMapperService.toLoanDTO(loan);
	    loanValidationService.validateLoanStatusBeforePayment(loanDTO);

	    loanDTO.setStatusPayment("PAID");
	    Loan updatedLoan = loanMapperService.toLoanEntity(loanDTO);
	    loanRepositoryWrapper.saveLoan(updatedLoan);
	}
}
