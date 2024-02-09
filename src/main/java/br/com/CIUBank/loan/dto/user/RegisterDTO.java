package br.com.CIUBank.loan.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.CIUBank.loan.entity.user.PersonRole;

public record RegisterDTO(String nome, String identificador, LocalDate dataNascimento,  String tipoIdentificador, BigDecimal valorMinimoMensalDasParcelas,
		BigDecimal valorMaximoMensalDasParcelas, String login, String password, PersonRole role) {
}