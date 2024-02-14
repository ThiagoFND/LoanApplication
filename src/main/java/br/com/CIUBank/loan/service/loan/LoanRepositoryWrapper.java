package br.com.CIUBank.loan.service.loan;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.entity.loan.Loan;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.repositories.LoanRepository;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class LoanRepositoryWrapper {

	private final LoanRepository loanRepository;
	private final PersonRepository personRepository;

	public LoanRepositoryWrapper(LoanRepository loanRepository, PersonRepository personRepository) {
		super();
		this.loanRepository = loanRepository;
		this.personRepository = personRepository;
	}

	public Person findPersonById(String userId) {
		return personRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."));
	}

	public Loan saveLoan(Loan entity) {
		return loanRepository.save(entity);
	}

	public Optional<Loan> findById(String id) {
		return loanRepository.findById(id);
	}

	public void deleteLoan(Loan loan) {
		loanRepository.delete(loan);
	}

	public List<Loan> findByPersonId(String userId) {
		return loanRepository.findByPersonId(userId);
	}
}
