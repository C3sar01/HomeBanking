package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    private boolean status;
    private int points;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Card> cards = new HashSet<>();

    public Client(){}

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.status = true;
        this.points = points;
    }


    public Set<Loan> getLoan(){
        return this.clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toSet());
    }


    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }



    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addCard(Card card) {
        card.setClient(this);
        cards.add(card);
    }



    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
