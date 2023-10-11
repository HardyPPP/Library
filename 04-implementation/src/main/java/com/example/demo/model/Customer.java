package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.model.RentalStatus.RENTED;


@Entity
public class Customer {
    // this class is mainly in charge of
    // storing the basic information of customer

    private Long customerId;
    // the customer id

    private String name;
    // the customer name

    private String phoneNumber;
    // the phone number of this customer

    private String email;
    // the email address of this customer

    private String address;
    // the address of this customer

    private LocalDate dateOfBirth;
    // the date of birth of this customer

    private int maxRental;
    // the maximum times that this customer could rent

    private List<Book> books = new ArrayList<>();

    private int lateFees;
    // the fees that this customer should pay


    private List<BookRental> bookRentals = new ArrayList<>();
    // the list of this customer's rental record


    public Customer(String name, String email, String address, LocalDate dateOfBirth, int maxRental, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.maxRental = maxRental;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {

    }

    @Column(name = "email")
    // get the email address of this customer
    public String getEmail() {
        return email;
    }

    @Column(name = "address")
    // get the address of this customer
    public String getAddress() {
        return address;
    }

    @Column(name = "date_of_birth")
    // get the date of birth of this customer
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Column(name = "max_rental")
    // get the maximum times that this customer could rent
    public int getMaxRental() {
        return maxRental;
    }

    @Column(name = "late_fees")
    // get the fees that this customer should pay
    public int getLateFees() {
        return lateFees;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_rental_book_rental_id")
    // get the list of this customer's rental record
    public List<BookRental> getBookRentals() {
        return bookRentals;
    }

    @ManyToMany(mappedBy = "waitingList")
    public List<Book> getBooks() {
        return books;
    }

    @Column(name = "name")
    // get the name of this customer
    public String getName() {
        return name;
    }

    @Column(name = "phone_number")
    // get the phone number of this customer
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Id
    // promary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    // get the id of this customer
    public Long getCustomerId() {
        return customerId;
    }

    public void addRental(BookRental bookRental) {
        // add a rental record to the list
        bookRentals.add(bookRental);
    }

    public int CurrentRentals() {
        // get the number of the rental records that not returned of this customer
        int count = 0;
        for (BookRental br : bookRentals) {
            if (br.getStatus() == RENTED) {
                count += 1;
            }
        }
        return count;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setMaxRental(int maxRental) {
        this.maxRental = maxRental;
    }

    public void setBookRentals(List<BookRental> bookRentals) {
        this.bookRentals = bookRentals;
    }


    public void setLateFees(int lateFees) {
        this.lateFees = lateFees;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBooks(List<Book> books) {
        this.books = books;

    }
}

