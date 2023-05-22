package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreateDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication,
                                             @RequestBody LoanApplicationDTO loanApplicationDTO){
        //Comprobar si el cliente esta autenticado
        Client clientConnect = clientRepository.findByEmail(authentication.getName());
        if(clientConnect == null) return new ResponseEntity<>("Client its not authenticated", HttpStatus.FORBIDDEN);
        System.out.println(loanApplicationDTO.toString());

        //Se verifica que los datos no esten vacios
        if(loanApplicationDTO.getLoanId() == 0 || loanApplicationDTO.getPayments() == 0 ||
                loanApplicationDTO.getToAccountNumber().isEmpty() ||
                loanApplicationDTO.getAmount() <= 0)
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        //Comprueba la existencia del tipo de prestamo
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).get();
        if(loan == null) return new ResponseEntity<>("No existe ese tipo de prestamo", HttpStatus.FORBIDDEN);

        //Comprobacion de que no exceda el monto máximo
        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()) return new ResponseEntity<>("Prestamo excedido", HttpStatus.FORBIDDEN);

        //Cantidad de cuotas validas
        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())) return new ResponseEntity<>("Cantidad de cuotas no permitidas", HttpStatus.FORBIDDEN);

        //Comprobar si la cuenta de destino existe
        Account accountDestino = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        if(accountDestino == null) return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);

        //Comprobar que la cuenta de destino pertenezca al cliente autenticado
        if(!clientConnect.getAccounts().contains(accountDestino)) return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);;

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.2,loanApplicationDTO.getPayments());

        clientConnect.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);

        clientLoanRepository.save(clientLoan);

        Transaction transactionDestino = new Transaction(TransactionType.CREDITO, loanApplicationDTO.getAmount(), loan.getName()+" - loan approved", LocalDateTime.now());

        accountDestino.addTransaction(transactionDestino);
        transactionRepository.save(transactionDestino);

        accountDestino.setBalance(accountDestino.getBalance()+loanApplicationDTO.getAmount());
        accountRepository.save(accountDestino);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Transactional
    @PostMapping ("/loansType")
    public ResponseEntity<Object> createLoan(@RequestBody LoanCreateDTO loanCreateDTO){
        if (loanCreateDTO.getName()==null){
            return new ResponseEntity<>("Debe seleccionar un nombre para el tipo de prestamo", HttpStatus.FORBIDDEN);
        }
        if (loanCreateDTO.getMaxAmount()<=0 || loanCreateDTO.getMaxAmount()==null){
            return new ResponseEntity<>("Debe seleccionar un monto maximo positivo", HttpStatus.FORBIDDEN);
        }
        if (loanCreateDTO.getPayments().size()==0){
            return new ResponseEntity<>("Debe agregar al menos una cuota disponible", HttpStatus.FORBIDDEN);
        }
        Loan loan= new Loan(loanCreateDTO.getName(), loanCreateDTO.getMaxAmount(), loanCreateDTO.getPayments());
        loanRepository.save(loan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/loans")
    public List<LoanDTO> getAll(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }
}
