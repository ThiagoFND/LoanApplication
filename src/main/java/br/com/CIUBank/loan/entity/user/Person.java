package br.com.CIUBank.loan.entity.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "person")
@Entity
@Setter @Getter @EqualsAndHashCode
public class Person implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "name", nullable = false, length = 50)
	@NotNull(message = "Name cannot be null")
	@Size(max = 50, message = "The name must be up to 50 characters long")
	private String name;

	@Column(name = "identifier", nullable = false, length = 50)
	@NotNull(message = "The identifier cannot be null")
	@Size(max = 50, message = "The identifier must be up to 50 characters long")
	private String identifier;

	@NotNull(message = "The date of birth cannot be null")
	private LocalDate birthDate;

	@Column(name = "typeIdentifier", nullable = false, length = 50)
	@NotNull(message = "The identifier cannot be null")
	@Size(max = 50, message = "The Identifier Type must have up to 50 characters")
	private String typeIdentifier;

	@Column(name = "minimumMonthlyValueOfInstallments", nullable = false, precision = 18, scale = 4)
	private BigDecimal minimumMonthlyValueOfInstallments;

	@Column(name = "maximumValueOfAllLoan", nullable = false, precision = 18, scale = 4)
	private BigDecimal maximumValueOfAllLoan;

	@Column(name = "active")
	private String active;
	private String login;
	private String password;
	private PersonRole role;

	public Person() {
		super();
	}

	public Person(
			@NotNull(message = "Name cannot be null") @Size(max = 50, message = "The name must be up to 50 characters long") String name,
			@NotNull(message = "The identifier cannot be null") @Size(max = 50, message = "The identifier must be up to 50 characters long") String identifier,
			@NotNull(message = "The date of birth cannot be null") LocalDate birthDate,
			@NotNull(message = "The identifier cannot be null") @Size(max = 50, message = "The Identifier Type must have up to 50 characters") String typeIdentifier,
			BigDecimal minimumMonthlyValueOfInstallments, BigDecimal maximumValueOfAllLoan, String active, String login,
			String password, PersonRole role) {
		this.name = name;
		this.identifier = identifier;
		this.birthDate = birthDate;
		this.typeIdentifier = typeIdentifier;
		this.minimumMonthlyValueOfInstallments = minimumMonthlyValueOfInstallments;
		this.maximumValueOfAllLoan = maximumValueOfAllLoan;
		this.active = "ACTIVE";
		this.login = login;
		this.password = password;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
	}

	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
