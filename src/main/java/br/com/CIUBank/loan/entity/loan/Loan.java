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

@Table(name = "Loan")
@Entity
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
		super();
		this.valueLoan = valueLoan;
		this.numberParcels = numberParcels;
		this.statusPayment = statusPayment;
		this.dateCreation = dateCreation;
		this.person = person;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getValueLoan() {
		return valueLoan;
	}

	public void setValueLoan(BigDecimal valueLoan) {
		this.valueLoan = valueLoan;
	}

	public Integer getNumberParcels() {
		return numberParcels;
	}

	public void setNumberParcels(Integer numberParcels) {
		this.numberParcels = numberParcels;
	}

	public String getStatusPayment() {
		return statusPayment;
	}

	public void setStatusPayment(String statusPayment) {
		this.statusPayment = statusPayment;
	}

	public LocalDate getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDate dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numberParcels == null) ? 0 : numberParcels.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((statusPayment == null) ? 0 : statusPayment.hashCode());
		result = prime * result + ((valueLoan == null) ? 0 : valueLoan.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Loan other = (Loan) obj;
		if (dateCreation == null) {
			if (other.dateCreation != null)
				return false;
		} else if (!dateCreation.equals(other.dateCreation))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numberParcels == null) {
			if (other.numberParcels != null)
				return false;
		} else if (!numberParcels.equals(other.numberParcels))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (statusPayment == null) {
			if (other.statusPayment != null)
				return false;
		} else if (!statusPayment.equals(other.statusPayment))
			return false;
		if (valueLoan == null) {
			if (other.valueLoan != null)
				return false;
		} else if (!valueLoan.equals(other.valueLoan))
			return false;
		return true;
	}

}
