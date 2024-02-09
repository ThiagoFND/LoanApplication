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

@Table(name = "users")
@Entity
public class Person implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "nome", nullable = false, length = 50)
	@NotNull(message = "O nome não pode ser nulo")
	@Size(max = 50, message = "O nome deve ter até  50 caracteres")
	private String nome;

	@Column(name = "identificador", nullable = false, length = 50)
	@NotNull(message = "O identificador não pode ser nulo")
	@Size(max = 50, message = "O identificador deve ter até  50 caracteres")
	private String identificador;

	@NotNull(message = "A data de nascimento não pode ser nula")
	private LocalDate dataNascimento;

	@Column(name = "tipoIdentificador", nullable = false, length = 50)
	@NotNull(message = "O identificador não pode ser nulo")
	@Size(max = 50, message = "O Tipo Identificador deve ter até  50 caracteres")
	private String tipoIdentificador;

	@Column(name = "valor_minimo_mensal_das_parcelas", nullable = false, precision = 18, scale = 4)
	private BigDecimal valorMinimoMensalDasParcelas;

	@Column(name = "valor_maximo_de_todo_o_emprestimo", nullable = false, precision = 18, scale = 4)
	private BigDecimal valorMaximoDeTodoOEmprestimo;
	private String login;
	private String password;
	private PersonRole role;

	public Person() {
		super();
	}

	public Person(
			@NotNull(message = "O nome não pode ser nulo") @Size(max = 50, message = "O nome deve ter até  50 caracteres") String nome,
			@NotNull(message = "O identificador não pode ser nulo") @Size(max = 50, message = "O identificador deve ter até  50 caracteres") String identificador,
			@NotNull(message = "A data de nascimento não pode ser nula") LocalDate dataNascimento,
			@NotNull(message = "O identificador não pode ser nulo") @Size(max = 50, message = "O Tipo Identificador deve ter até  50 caracteres") String tipoIdentificador,
			BigDecimal valorMinimoMensalDasParcelas, BigDecimal valorMaximoDeTodoOEmprestimo, String login,
			String password, PersonRole role) {
		super();
		this.nome = nome;
		this.identificador = identificador;
		this.dataNascimento = dataNascimento;
		this.tipoIdentificador = tipoIdentificador;
		this.valorMinimoMensalDasParcelas = valorMinimoMensalDasParcelas;
		this.valorMaximoDeTodoOEmprestimo = valorMaximoDeTodoOEmprestimo;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getTipoIdentificador() {
		return tipoIdentificador;
	}

	public void setTipoIdentificador(String tipoIdentificador) {
		this.tipoIdentificador = tipoIdentificador;
	}

	public BigDecimal getValorMinimoMensalDasParcelas() {
		return valorMinimoMensalDasParcelas;
	}

	public void setValorMinimoMensalDasParcelas(BigDecimal valorMinimoMensalDasParcelas) {
		this.valorMinimoMensalDasParcelas = valorMinimoMensalDasParcelas;
	}

	public BigDecimal getValorMaximoDeTodoOEmprestimo() {
		return valorMaximoDeTodoOEmprestimo;
	}

	public void setValorMaximoDeTodoOEmprestimo(BigDecimal valorMaximoDeTodoOEmprestimo) {
		this.valorMaximoDeTodoOEmprestimo = valorMaximoDeTodoOEmprestimo;
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
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (this.role == PersonRole.ADMIN) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
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
