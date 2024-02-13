package br.com.CIUBank.loan.service.loan;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        logger.info("Finding all loans for the authenticated user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = ((Person) authentication.getPrincipal()).getId();
            List<Loan> userLoans = loanRepository.findByPersonId(userId);
            List<LoanDTO> loanDTOs = DozerMapper.parseListObjects(userLoans, LoanDTO.class);
            logger.info("Found " + loanDTOs.size() + " loans for the authenticated user.");
            return loanDTOs;
        } else {
            throw new BusinessException("User is not authenticated.");
        }
    }

    public Optional<LoanDTO> findById(String id) {
        logger.info("Finding loan by ID: " + id);
        return loanRepository.findById(id).map(loan -> {
            logger.info("Loan found with ID: " + id + " for user ID: " + loan.getPerson().getId());
            authorizeAccess(loan.getPerson().getId().toString());
            return DozerMapper.parseObject(loan, LoanDTO.class);
        });
    }


    @Transactional
    public LoanDTO create(LoanDTO loanDTO) {
        logger.info("Creating a loan for user ID: " + loanDTO.getPerson().getId());
        validateLoanDTO(loanDTO);

        var user = personRepository.findById(loanDTO.getPerson().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."));
        var maxLoanValue = identificadorService.getMaxLoanValueForUser(user.getId());
        if (loanDTO.getValueLoan().compareTo(maxLoanValue) > 0) {
            logger.warning("Attempt to exceed max loan value for user ID: " + loanDTO.getPerson().getId());
            throw new BusinessException("The loan amount exceeds the maximum allowed value for this user.");
        }

        var entity = DozerMapper.parseObject(loanDTO, Loan.class);
        entity.setPerson(user);
        var savedLoan = loanRepository.save(entity);
        logger.info("Loan created with ID: " + savedLoan.getId() + " for user ID: " + loanDTO.getPerson().getId());
        return DozerMapper.parseObject(savedLoan, LoanDTO.class);
    }

    @Transactional
    public void delete(String id) {
        logger.info("Attempting to delete loan with ID: " + id);
        var loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        authorizeAccess(loan.getPerson().getId().toString());
        loanRepository.delete(loan);
        logger.info("Loan with ID: " + id + " successfully deleted.");
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
