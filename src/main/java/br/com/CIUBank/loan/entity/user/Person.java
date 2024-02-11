package br.com.CIUBank.loan.entity.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

@Table(name = "person")
@Entity
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
		super();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getTypeIdentifier() {
		return typeIdentifier;
	}

	public void setTypeIdentifier(String typeIdentifier) {
		this.typeIdentifier = typeIdentifier;
	}

	public BigDecimal getMinimumMonthlyValueOfInstallments() {
		return minimumMonthlyValueOfInstallments;
	}

	public void setMinimumMonthlyValueOfInstallments(BigDecimal minimumMonthlyValueOfInstallments) {
		this.minimumMonthlyValueOfInstallments = minimumMonthlyValueOfInstallments;
	}

	public BigDecimal getMaximumValueOfAllLoan() {
		return maximumValueOfAllLoan;
	}

	public void setMaximumValueOfAllLoan(BigDecimal maximumValueOfAllLoan) {
		this.maximumValueOfAllLoan = maximumValueOfAllLoan;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PersonRole getRole() {
		return role;
	}

	public void setRole(PersonRole role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((maximumValueOfAllLoan == null) ? 0 : maximumValueOfAllLoan.hashCode());
		result = prime * result
				+ ((minimumMonthlyValueOfInstallments == null) ? 0 : minimumMonthlyValueOfInstallments.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((typeIdentifier == null) ? 0 : typeIdentifier.hashCode());
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
		Person other = (Person) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (maximumValueOfAllLoan == null) {
			if (other.maximumValueOfAllLoan != null)
				return false;
		} else if (!maximumValueOfAllLoan.equals(other.maximumValueOfAllLoan))
			return false;
		if (minimumMonthlyValueOfInstallments == null) {
			if (other.minimumMonthlyValueOfInstallments != null)
				return false;
		} else if (!minimumMonthlyValueOfInstallments.equals(other.minimumMonthlyValueOfInstallments))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		if (typeIdentifier == null) {
			if (other.typeIdentifier != null)
				return false;
		} else if (!typeIdentifier.equals(other.typeIdentifier))
			return false;
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (this.role == PersonRole.USER) {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
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
