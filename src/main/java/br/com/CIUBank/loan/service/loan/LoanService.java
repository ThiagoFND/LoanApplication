package br.com.CIUBank.loan.service.loan;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.dto.user.PersonDTO;
import br.com.CIUBank.loan.entity.loan.Loan;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.BusinessException;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.mapper.DozerMapper;
import br.com.CIUBank.loan.repositories.LoanRepository;
import br.com.CIUBank.loan.repositories.PersonRepository;
import br.com.CIUBank.loan.service.user.AuthorizationService;
import br.com.CIUBank.loan.service.user.IdentificadorService;

@Service
public class LoanService {

	private Logger logger = Logger.getLogger(LoanService.class.getName());

	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private IdentificadorService identificadorService;
	@Autowired
	private AuthorizationService authorizationService;

	public List<LoanDTO> findAll() {

		logger.info("Finding all loans!");

		return DozerMapper.parseListObjects(loanRepository.findAll(), LoanDTO.class);
	}

	public LoanDTO findById(String id) {
		logger.info("Finding one loan!");

		Loan loan = loanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		if (!authorizationService.isUserOwnerOfId(loan.getPerson().getId().toString())) {
			throw new BusinessException("User is not authorized to view this loan.");
		}

		return DozerMapper.parseObject(loan, LoanDTO.class);
	}

	public LoanDTO create(LoanDTO loans) {
		logger.info("Creating one loan!");

		PersonDTO personDTO = loans.getPerson();
		if (personDTO == null || personDTO.getId() == null) {
			throw new ResourceNotFoundException("UserDTO ID is required.");
		}

		if (!authorizationService.isUserOwnerOfId(personDTO.getId())) {
			throw new BusinessException("User is not authorized to create this loan.");
		}

		Person user = personRepository.findById(personDTO.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."));

		BigDecimal maxLoanValue = identificadorService.getMaxLoanValueForUser(user.getId());
		if (loans.getValorEmprestimo().compareTo(maxLoanValue) > 0) {
			throw new BusinessException("The loan amount exceeds the maximum allowed value for this user.");
		}

		Loan entity = DozerMapper.parseObject(loans, Loan.class);
		entity.setPerson(user);

		var savedLoan = loanRepository.save(entity);
		return DozerMapper.parseObject(savedLoan, LoanDTO.class);
	}

	public void delete(String id) {
		logger.info("Deleting one loan!");

		Loan loan = loanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		if (!authorizationService.isUserOwnerOfId(loan.getPerson().getId().toString())) {
			throw new BusinessException("User is not authorized to delete this loan.");
		}

		loanRepository.delete(loan);
	}

}
