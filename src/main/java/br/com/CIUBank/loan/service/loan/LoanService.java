package br.com.CIUBank.loan.service.loan;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.entity.loan.Loan;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.BusinessException;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.mapper.DozerMapper;
import br.com.CIUBank.loan.repositories.LoanRepository;
import br.com.CIUBank.loan.repositories.PersonRepository;
import br.com.CIUBank.loan.service.user.AuthorizationService;
import br.com.CIUBank.loan.service.user.IdentificatorService;
import jakarta.transaction.Transactional;

@Service
public class LoanService {

    private final Logger logger = Logger.getLogger(LoanService.class.getName());
    private final LoanRepository loanRepository;
    private final PersonRepository personRepository;
    private final IdentificatorService identificadorService;
    private final AuthorizationService authorizationService;

    public LoanService(LoanRepository loanRepository, PersonRepository personRepository,
                       IdentificatorService identificadorService, AuthorizationService authorizationService) {
        this.loanRepository = loanRepository;
        this.personRepository = personRepository;
        this.identificadorService = identificadorService;
        this.authorizationService = authorizationService;
    }

    public List<LoanDTO> findAll() {
        logger.info("Finding all loans");
        return DozerMapper.parseListObjects(loanRepository.findAll(), LoanDTO.class);
    }

    public LoanDTO findById(String id) {
        logger.info("Finding loan by ID: " + id);
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        authorizeAccess(loan.getPerson().getId().toString());

        return DozerMapper.parseObject(loan, LoanDTO.class);
    }

    @Transactional
    public LoanDTO create(LoanDTO loanDTO) {
        logger.info("Creating a loan");
        validateLoanDTO(loanDTO);

        Person user = personRepository.findById(loanDTO.getPerson().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."));

        BigDecimal maxLoanValue = identificadorService.getMaxLoanValueForUser(user.getId());
        if (loanDTO.getValueLoan().compareTo(maxLoanValue) > 0) {
            throw new BusinessException("The loan amount exceeds the maximum allowed value for this user.");
        }

        Loan entity = DozerMapper.parseObject(loanDTO, Loan.class);
        entity.setPerson(user);

        var savedLoan = loanRepository.save(entity);
        return DozerMapper.parseObject(savedLoan, LoanDTO.class);
    }

    @Transactional
    public void delete(String id) {
        logger.info("Deleting loan with ID: " + id);
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        authorizeAccess(loan.getPerson().getId().toString());

        loanRepository.delete(loan);
    }

    private void authorizeAccess(String userId) {
        if (!authorizationService.isUserOwnerOfId(userId)) {
            logger.warning("Unauthorized access attempt for loan associated with user ID: " + userId);
            throw new BusinessException("User is not authorized for this operation.");
        }
    }

    private void validateLoanDTO(LoanDTO loanDTO) {
        if (loanDTO.getPerson() == null || loanDTO.getPerson().getId() == null) {
            throw new IllegalArgumentException("Person ID is required in the loan DTO.");
        }
        authorizeAccess(loanDTO.getPerson().getId());
    }
}
