package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class BookRental {
    // this class is mainly in charge of
    // storing the basic information of book rental record

    private LocalDate checkOutDate;
    // the date of this book copy rented

    private LocalDate expectedReturnDate;
    //the date of this cook copy should be returned

    private Integer lateFees;
    // the fee of the customer need to pay

    private LocalDate actualReturnDate;
    // the actual return date of this copy

    private RentalStatus status;
    // if the book is returned

    private BookCopy bookCopy;
    // the copy rented

    private Customer customer;
    // the customer who rent the book

    private Long bookRentalId;
    // the id of this rental record

    public BookRental(LocalDate checkOutDate, LocalDate expectedReturnDate, BookCopy bookCopy, Customer customer) {
        this.checkOutDate = checkOutDate;
        this.expectedReturnDate = expectedReturnDate;
        this.bookCopy = bookCopy;
        this.customer = customer;
        this.status = RentalStatus.RENTED;
    }

    public BookRental() {

    }

    @Column(name = "check_out_date")
    // get the date of this book copy rented
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    @Column(name = "expected_return_date")
    // get the date of this cook copy should be returned
    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    @Column(name = "late_fees")
    // get the fee of the customer need to pay
    public Integer getLateFees() {
        return lateFees;
    }

    @Column(name = "actual_return_date")
    //get the actual return date of this copy
    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    @Column(name = "status")
    // check if the book is returned
    public RentalStatus getStatus() {
        return status;
    }

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_customer_id")
    // get the customer who rent the book
    public Customer getCustomer() {
        return customer;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_copy_book_copy_id")
    // get the copy rented
    public BookCopy getBookCopy() {
        return bookCopy;
    }

    @Id
    // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_rental_id")
    // get the id of this rental record
    public Long getBookRentalId() {
        return bookRentalId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public void setLateFees(Integer lateFees) {
        this.lateFees = lateFees;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public void setBookRentalId(Long bookRentalId) {
        this.bookRentalId = bookRentalId;
    }
}
