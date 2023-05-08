package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client melba = new Client();
			melba.setFirstName("melba");
			melba.setLastName("morel");
			melba.setEmail("melba@gmail.com");

			Client ana = new Client();
			ana.setFirstName("Ana");
			ana.setLastName("Arias");
			ana.setEmail("ana@gmail.com");

			clientRepository.save(ana);

			clientRepository.save(melba);

			Account account1 = new Account();
			account1.setNumber("VIN001");
			account1.setClient(melba);
			account1.setCreationDate(LocalDateTime.now());
			account1.setBalance(5000.0);

			accountRepository.save(account1);

			Account account2 = new Account();
			account2.setNumber("VIN002");
			account2.setClient(melba);
			account2.setCreationDate(LocalDateTime.now());
			account2.setBalance(7500.0);

			accountRepository.save(account2);


			//Client melba = new Client(repository.save(new Account());)


		};
	}


}
