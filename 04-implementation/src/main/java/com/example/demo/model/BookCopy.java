package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BookCopy {
    // this class is mainly in charge of
    // storing the basic information of book copies

    private Long bookCopyId;
    // the id of this copy

    private Boolean available;
    // if the copy is return to the library

    private LocalDate expectedReturnDate;
    // the expected return date of this book

    private List<BookRental> bookRental = new ArrayList<>();
    // the rent record of this copy

    private Book book;
    // the original book of this copy

    public BookCopy(Book book) {
        this.book = book;
        this.available = true;
    }
    public BookCopy() {

    }
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_rental_book_rental_id")
    // get the rent record of this copy
    public List<BookRental> getBookRental() {
        return bookRental;
    }

    @Id
    // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_copy_id", nullable = false)
    // get the id of this copy
    public Long getBookCopyId() {
        return bookCopyId;
    }

    @Column(name="available")
    // check if the copy is rented
    public Boolean getAvailable() {
        return available;
    }

    @Column(name="expected_return_date")
    // get the expected return date of this book
    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    @ManyToOne
    @JoinColumn(name = "book_book_id")
    // get the original book of this copy
    public Book getBook() {
        return book;
    }

    public void setBookRental(List<BookRental> bookRental) {
        this.bookRental = bookRental;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setBookCopyId(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

}
