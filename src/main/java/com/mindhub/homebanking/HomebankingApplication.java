package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository) {
		return (args) -> {

			//Clientes
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

			//Cuentas
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

			Account account3 = new Account();
			account3.setNumber("VIN003");
			account3.setClient(ana);
			account3.setCreationDate(LocalDateTime.now());
			account3.setBalance(8000.0);

			accountRepository.save(account3);

			//Transacciones
			Transaction transaction1 = new Transaction();
			transaction1.setType(TransactionType.DEBITO);
			transaction1.setAmount(-5000);
			transaction1.setAccount(account2);
			transaction1.setDate(LocalDateTime.now());
			transaction1.setDescription("Transacccion hecha desde la cuenta 2");
			transactionRepository.save(transaction1);


			Transaction transaction2 = new Transaction();
			transaction2.setType(TransactionType.CREDITO);
			transaction2.setAmount(-10000);
			transaction2.setAccount(account2);
			transaction2.setDate(LocalDateTime.now());
			transaction2.setDescription("Transaccion hecha desde la cuenta 2");
			transactionRepository.save(transaction2);



			Transaction transaction3 = new Transaction();
			transaction3.setType(TransactionType.DEBITO);
			transaction3.setAmount(-7000);
			transaction3.setAccount(account3);
			transaction3.setDate(LocalDateTime.now());
			transaction3.setDescription("Transaccion hecha desde la cuenta 3");
			transactionRepository.save(transaction3);

			Transaction transaction4 = new Transaction();
			transaction4.setType(TransactionType.CREDITO);
			transaction4.setAmount(-2000);
			transaction4.setAccount(account1);
			transaction4.setDate(LocalDateTime.now());
			transaction4.setDescription("Transaccion hecha desde la cuenta 1");
			transactionRepository.save(transaction4);




		};
	}


}
