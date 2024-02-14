package br.com.CIUBank.loan.service.loan;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.exceptions.BusinessException;
import br.com.CIUBank.loan.service.user.AuthorizationService;

@Service
public class LoanAuthorizationService {

    private final AuthorizationService authorizationService;
    private final Logger logger = Logger.getLogger(LoanAuthorizationService.class.getName());

    public LoanAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public void authorizeAccessForUser(String userId) {
        if (!authorizationService.isUserOwnerOfId(userId)) {
            logger.warning("Unauthorized access attempt for loan associated with user ID: " + userId);
            throw new BusinessException("User is not authorized for this operation.");
        }
    }
}
