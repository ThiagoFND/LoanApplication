package br.com.CIUBank.loan.controller.user;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody; // Importação correta
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.dto.user.PasswordUpdateDTO;
import br.com.CIUBank.loan.service.user.PersonService;

@RestController
@RequestMapping("api/v1/user/pass")
public class PersonController {
    
    @Autowired
    private PersonService personService;

    private Logger logger = Logger.getLogger(PersonController.class.getName());

    @PatchMapping("/changePassword/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        logger.info("Password update request received");

        if (passwordUpdateDTO.getOldPassword() == null || passwordUpdateDTO.getNewPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The old password and the new password cannot be null.");
        }

        personService.updatePassword(id, passwordUpdateDTO.getOldPassword(), passwordUpdateDTO.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }
}
