package br.com.CIUBank.loan.controller.loan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/v1/user/loan")
public class LoanController {

	@Autowired
	private LoanService loanServices;
	
    @PostMapping(value = "/",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public LoanDTO create(@RequestBody LoanDTO loans) {
		return loanServices.create(loans);
	}

	@GetMapping(value = "/",
		produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LoanDTO> findAll() {
		return loanServices.findAll();
	}
	
    @GetMapping("/findid/{id}")
    public ResponseEntity<LoanDTO> findById(@PathVariable String id) {
    	LoanDTO setorDTO = loanServices.findById(id);
        return ResponseEntity.ok(setorDTO);
    }

    @DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
    	loanServices.delete(id);
		return ResponseEntity.noContent().build();
	}
}
