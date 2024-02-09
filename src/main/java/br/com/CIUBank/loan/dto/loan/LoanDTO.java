package br.com.CIUBank.loan.dto.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.CIUBank.loan.dto.user.PersonDTO;
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
public class LoanDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	// Para valorEmprestimo
	@Column(name = "valor_emprestimo", nullable = false, precision = 18, scale = 4)
	@NotNull(message = "O valor do empréstimo não pode ser nulo")
	@Digits(integer=14, fraction=4, message = "O valor do empréstimo deve ter até 14 dígitos inteiros e até 4 dígitos fracionários.")
	private BigDecimal valorEmprestimo;

	// Para numeroParcelas (sem alterações necessárias)
	@Column(name = "numero_parcelas", nullable = false)
	@NotNull(message = "O número das parcelas não pode ser nulo")
	private Integer numeroParcelas;

	// Para statusPagamento
	@Column(name = "status_pagamento", nullable = false, length = 50)
	@NotNull(message = "O status do pagamento não pode ser nulo")
	@Size(max = 50, message = "O status do pagamento deve ter até 50 caracteres")
	private String statusPagamento;

	// Para dataCriacao
	@Column(name = "data_criacao", nullable = false)
	@NotNull(message = "A data de criação não pode ser nula")
	private LocalDate dataCriacao;

	@ManyToOne
	@JoinColumn(nullable = false)
	private PersonDTO person;
	
	public LoanDTO() {
		super();
	}

	public LoanDTO(BigDecimal valorEmprestimo, Integer numeroParcelas, String statusPagamento, LocalDate dataCriacao,
			PersonDTO person) {
		super();
		this.valorEmprestimo = valorEmprestimo;
		this.numeroParcelas = numeroParcelas;
		this.statusPagamento = statusPagamento;
		this.dataCriacao = dataCriacao;
		this.person = person;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PersonDTO getPerson() {
		return person;
	}

	public void setPerson(PersonDTO person) {
		this.person = person;
	}

	public BigDecimal getValorEmprestimo() {
		return valorEmprestimo;
	}

	public void setValorEmprestimo(BigDecimal valorEmprestimo) {
		this.valorEmprestimo = valorEmprestimo;
	}

	public Integer getNumeroParcelas() {
		return numeroParcelas;
	}

	public void setNumeroParcelas(Integer numeroParcelas) {
		this.numeroParcelas = numeroParcelas;
	}

	public String getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(String statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numeroParcelas == null) ? 0 : numeroParcelas.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((statusPagamento == null) ? 0 : statusPagamento.hashCode());
		result = prime * result + ((valorEmprestimo == null) ? 0 : valorEmprestimo.hashCode());
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
		LoanDTO other = (LoanDTO) obj;
		if (dataCriacao == null) {
			if (other.dataCriacao != null)
				return false;
		} else if (!dataCriacao.equals(other.dataCriacao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numeroParcelas == null) {
			if (other.numeroParcelas != null)
				return false;
		} else if (!numeroParcelas.equals(other.numeroParcelas))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (statusPagamento == null) {
			if (other.statusPagamento != null)
				return false;
		} else if (!statusPagamento.equals(other.statusPagamento))
			return false;
		if (valorEmprestimo == null) {
			if (other.valorEmprestimo != null)
				return false;
		} else if (!valorEmprestimo.equals(other.valorEmprestimo))
			return false;
		return true;
	}

}
