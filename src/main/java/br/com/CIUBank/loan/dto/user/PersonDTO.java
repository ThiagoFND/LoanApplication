package br.com.CIUBank.loan.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.entity.user.PersonRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "person")
@Entity
@Setter @Getter @EqualsAndHashCode
public class PersonDTO implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String name;
	private String identifier;
	private LocalDate birthDate;
	private String typeIdentifier;
	private BigDecimal minimumMonthlyValueOfInstallments;
	private BigDecimal maximumValueOfAllLoan;
	private String active;
	private String login;
	private String password;
	private PersonRole role;

	public PersonDTO() {
		super();
	}

	public PersonDTO(String name, String identifier, LocalDate birthDate, String typeIdentifier,
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
