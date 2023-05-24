package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

//	@Autowired
//	PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private TransactionService transactionService;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository
	) {
		return (args) -> {
//			//Clientes
//			Client client1 = new Client("Melba", "Lorenzo", "melba@gmail.com",passwordEncoder.encode("1234"));
//			Client client2 = new Client("Admin", "Admin", "admin@gmail.com",passwordEncoder.encode("1234"));
//			Client client3 = new Client("root", "rootLasName", "root@gmail.com",passwordEncoder.encode("1234"));
//
//			//Cuentas
//			Account account1 = new Account("VIN001",LocalDateTime.now(),5000.00);
//			Account account2 = new Account("VIN002",LocalDateTime.now().plusDays(1),7500.00);
//			Account account3 = new Account("VIN003",LocalDateTime.now(),10000.00);
//
//			//Transacciones
//			Transaction transaction1 = new Transaction(TransactionType.DEBITO, 1000.00, "Cuota", LocalDateTime.now());
//			Transaction transaction2 = new Transaction(TransactionType.CREDITO,2000.00,"Pago",LocalDateTime.now());
//
//			//Payments y Loans
//			List<Integer> payments1 = new ArrayList<>(Arrays.asList(12,24,36,48,60));
//			Loan loan1 = new Loan("Hipotecario",500.000,payments1);
//			List<Integer> payments2 = new ArrayList<>(Arrays.asList(6,12,24));
//			Loan loan2 = new Loan("Personal",100.000,payments2);
//			List<Integer> payments3 = new ArrayList<>(Arrays.asList(6,12,24,36));
//			Loan loan3 = new Loan("Automotriz",300.000,payments3);
//
//			ClientLoan clientLoan1 = new ClientLoan(50000.00,12);
//			ClientLoan clientLoan2 = new ClientLoan(10000.00,6);
//
//			Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.CREDITO ,CardColor.TITANIUM, "1234-5678-9123-4567",123, LocalDate.now(), LocalDate.now().plusYears(5));
//			Card card2 = new Card(client2.getFirstName()+" "+client2.getLastName(),CardType.CREDITO ,CardColor.SILVER, "5756-1256-8790-1745", 567, LocalDate.now(), LocalDate.now().plusYears(4));
//			Card card3 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.DEBITO ,CardColor.GOLD, "8071-5490-1290-3052", 390, LocalDate.now(), LocalDate.now().plusYears(3));
//
//
//			client1.addAccount(account1);
//			client1.addAccount(account2);
//			client2.addAccount(account3);
//			account1.addTransaction(transaction1);
//			account1.addTransaction(transaction2);
//
//			client1.addClientLoan(clientLoan1);
//			loan1.addClientLoan(clientLoan1);
//			client1.addClientLoan(clientLoan2);
//			loan2.addClientLoan(clientLoan2);
//
//			client1.addCard(card1);
//			client2.addCard(card2);
//			client1.addCard(card3);
//
//
//
//
//
//			clientRepository.save(client1);
//			clientRepository.save(client2);
//			clientRepository.save(client3);
//			accountRepository.save(account1);
//			accountRepository.save(account2);
//			accountRepository.save(account3);
//			transactionRepository.save(transaction1);
//			transactionRepository.save(transaction2);
//			loanRepository.save(loan1);
//			loanRepository.save(loan2);
//			loanRepository.save(loan3);
//			clientLoanRepository.save(clientLoan1);
//			clientLoanRepository.save(clientLoan2);
//			cardRepository.save(card1);
//			cardRepository.save(card2);
//			cardRepository.save(card3);

//			for (int i = 0; i < 11; i++) {
//				Transaction transaction = new Transaction();
//				transaction.setDate(LocalDateTime.now());
//				transaction.setDescription("Compras");
//				transaction.setAmount(20000.00);
//				transaction.setType(TransactionType.CREDITO);
//				transactionRepository.save(transaction);
//			}




		};
	}


}
