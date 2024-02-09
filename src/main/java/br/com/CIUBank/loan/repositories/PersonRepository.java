package br.com.CIUBank.loan.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.CIUBank.loan.entity.user.Person;

public interface PersonRepository extends JpaRepository<Person, String> {
	UserDetails findByLogin(String login);
}