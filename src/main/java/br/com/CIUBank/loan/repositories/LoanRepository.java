package br.com.CIUBank.loan.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.CIUBank.loan.entity.loan.Loan;

public interface LoanRepository extends JpaRepository<Loan, String> {
	List<Loan> findByPersonId(String personId);
}
