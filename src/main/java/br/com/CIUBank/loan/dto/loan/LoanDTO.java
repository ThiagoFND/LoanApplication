package br.com.CIUBank.loan.dto.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.CIUBank.loan.dto.user.PersonDTO;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @EqualsAndHashCode
public class LoanDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private BigDecimal valueLoan;
	private Integer numberParcels;
	private String statusPayment;
	private LocalDate dateCreation;

	@ManyToOne
	@JoinColumn
	private PersonDTO person;

	public LoanDTO() {
		super();
	}

	public LoanDTO(BigDecimal valueLoan, Integer numberParcels, String statusPayment, LocalDate dateCreation,
			PersonDTO person) {
		super();
		this.valueLoan = valueLoan;
		this.numberParcels = numberParcels;
		this.statusPayment = statusPayment;
		this.dateCreation = dateCreation;
		this.person = person;
	}

}
