package br.com.CIUBank.loan.service.user;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.CIUBank.loan.dto.user.RegisterDTO;
import br.com.CIUBank.loan.entity.user.Person;
import br.com.CIUBank.loan.exceptions.ResourceNotFoundException;
import br.com.CIUBank.loan.repositories.PersonRepository;

@Service
public class IdentificadorService {

	@Autowired
	private PersonRepository userRepository;
	@Autowired
	private PersonRepository personRepository;

	// Métodos existentes: determinarTipoIdentificador, calcularValorMinimoParcelas,
	// calcularValorMaximoEmprestimo

	public boolean registrarUsuario(RegisterDTO data) {
		if (userRepository.findByLogin(data.login()) != null) {
			// Login já existe
			return false;
		}

		String tipoIdentificador = determinarTipoIdentificador(data.identificador());
		// Define automaticamente com base no tipo de identificador
		BigDecimal valorMinimoParcelas = calcularValorMinimoParcelas(tipoIdentificador);
		BigDecimal valorMaximoEmprestimo = calcularValorMaximoEmprestimo(tipoIdentificador);

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		// Cria o novo usuário com valores calculados automaticamente
		Person newUser = new Person(data.nome(), data.identificador(), data.dataNascimento(), tipoIdentificador,
				valorMinimoParcelas, // Usa o valor calculado
				valorMaximoEmprestimo, // Usa o valor calculado
				data.login(), encryptedPassword, data.role());

		userRepository.save(newUser);
		return true;
	}

	public String determinarTipoIdentificador(String identificador) {
		if (identificador == null) {
			return null;
		}
		switch (identificador.length()) {
		case 11:
			return "PF";
		case 14:
			return "PJ";
		case 8:
			return "EU";
		case 10:
			return "AP";
		default:
			return "Desconhecido";
		}
	}

	public BigDecimal calcularValorMinimoParcelas(String tipoIdentificador) {
		switch (tipoIdentificador) {
		case "PF":
			return BigDecimal.valueOf(300.00);
		case "PJ":
			return BigDecimal.valueOf(1000.00);
		case "EU":
			return BigDecimal.valueOf(100.00);
		case "AP":
			return BigDecimal.valueOf(400.00);
		default:
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal calcularValorMaximoEmprestimo(String tipoIdentificador) {
		switch (tipoIdentificador) {
		case "PF":
			return BigDecimal.valueOf(10000.00);
		case "PJ":
			return BigDecimal.valueOf(100000.00);
		case "EU":
			return BigDecimal.valueOf(10000.00);
		case "AP":
			return BigDecimal.valueOf(25000.00);
		default:
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal getMaxLoanValueForUser(String userId) {
		Person person = personRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for the given ID."));

		return person.getValorMaximoDeTodoOEmprestimo();
	}

}
