package br.com.CIUBank.loan.controller.loan;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.service.loan.LoanService;

@RestController
@RequestMapping("loan")
public class LoanController {

    private final LoanService loanServices;

    public LoanController(LoanService loanServices) {
        this.loanServices = loanServices;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanDTO> create(@RequestBody LoanDTO loans) {
        LoanDTO createdLoan = loanServices.create(loans);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LoanDTO>> findAll() {
        List<LoanDTO> loans = loanServices.findAll();
        return ResponseEntity.ok(loans);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanDTO> findById(@PathVariable String id) {
        Optional<LoanDTO> loanDTO = loanServices.findById(id);
        return loanDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        loanServices.markLoanAsInactive(id);
        return ResponseEntity.ok("Resource deleted successfully.");
    }

    @PatchMapping(value = "/payment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markLoanAsPaid(@PathVariable String id) {
        loanServices.markLoanAsPaid(id);
        return ResponseEntity.ok("Loan marked as successful payment.");
    }
}
