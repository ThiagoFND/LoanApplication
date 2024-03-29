package br.com.CIUBank.loan.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.CIUBank.loan.entity.user.PersonRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PersonDTO {
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

    // Constructor with parameters
    public PersonDTO(String id, String name, String identifier, LocalDate birthDate, String typeIdentifier,
                     BigDecimal minimumMonthlyValueOfInstallments, BigDecimal maximumValueOfAllLoan, String active,
                     String login, String password, PersonRole role) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.birthDate = birthDate;
        this.typeIdentifier = typeIdentifier;
        this.minimumMonthlyValueOfInstallments = minimumMonthlyValueOfInstallments;
        this.maximumValueOfAllLoan = maximumValueOfAllLoan;
        this.active = active;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
