package br.com.CIUBank.loan.entity.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.CIUBank.loan.entity.user.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Loan")
@Entity
@Setter @Getter @EqualsAndHashCode
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "valueLoan", nullable = false, precision = 18, scale = 4)
	@NotNull(message = "The loan amount cannot be zero")
	@Digits(integer = 14, fraction = 4, message = "The loan amount must be up to 14 whole digits and up to 4 fractional digits.")
	private BigDecimal valueLoan;

	@Column(name = "numberParcels", nullable = false)
	@NotNull(message = "The number of installments cannot be null")
	private Integer numberParcels;

	@Column(name = "statusPayment", nullable = false, length = 50)
	@NotNull(message = "Payment status cannot be null")
	@Size(max = 50, message = "Payment status must be up to 50 characters long")
	private String statusPayment;

	@Column(name = "dateCreation", nullable = false)
	@NotNull(message = "The creation date cannot be null")
	private LocalDate dateCreation;

	@ManyToOne
	@JoinColumn
	private Person person;

	public Loan() {
		super();
	}

	public Loan(
			@NotNull(message = "The loan amount cannot be zero") @Digits(integer = 14, fraction = 4, message = "The loan amount must be up to 14 whole digits and up to 4 fractional digits.") BigDecimal valueLoan,
			@NotNull(message = "The number of installments cannot be null") Integer numberParcels,
			@NotNull(message = "Payment status cannot be null") @Size(max = 50, message = "Payment status must be up to 50 characters long") String statusPayment,
			@NotNull(message = "The creation date cannot be null") LocalDate dateCreation, Person person) {
		this.valueLoan = valueLoan;
		this.numberParcels = numberParcels;
		this.statusPayment = statusPayment;
		this.dateCreation = dateCreation;
		this.person = person;
	}

}
