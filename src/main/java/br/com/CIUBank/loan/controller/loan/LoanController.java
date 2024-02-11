package br.com.CIUBank.loan.controller.loan;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.CIUBank.loan.dto.loan.LoanDTO;
import br.com.CIUBank.loan.service.loan.LoanService;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController {

	private final LoanService loanServices;
	
    public LoanController(LoanService loanServices) {
        this.loanServices = loanServices;
    }
    
    @PostMapping(value = "/",
    		consumes = MediaType.APPLICATION_JSON_VALUE,
    		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoanDTO> create(@RequestBody LoanDTO loans) {
		LoanDTO createdLoan = loanServices.create(loans);
		return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
	}

	@GetMapping(value = "/",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<LoanDTO>> findAll() {
		List<LoanDTO> loans = loanServices.findAll();
		return ResponseEntity.ok(loans);
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> findById(@PathVariable String id) {
    	try {
    	    LoanDTO loanDTO = loanServices.findById(id);
    	    return ResponseEntity.ok(loanDTO);
    	} catch (Exception e) {
    	    return ResponseEntity.notFound().build();
    	}
    }

    @DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) {
    	loanServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}
