package com.example.demo.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "book")
public class Book {
    // this class is mainly in charge of
    // storing the basic information of books

    private Long bookId;
    // book id

    private String title;
    // book title

    private String isbnNumber;
    // unique isbn number of a book

    private String section;
    // the section this book belongs to
    private String shelf;
    // the shelf this book belongs to

    private List<BookCopy> bookCopies = new ArrayList<>();
    // a list of the copies of this book

    private List<Author> authors = new ArrayList<>();
    // a list of the authors of this book

    private List<Customer> waitingList = new ArrayList<>();
    // a list of customer waiting for rent this book


    public Book(String title, String isbnNumber, List<Author> authors, String section, String shelf) {
        this.title = title;
        this.isbnNumber = isbnNumber;
        this.section = section;
        this.shelf = shelf;
        this.authors = authors;
    }

    public Book() {

    }

    @ManyToMany(cascade = CascadeType.ALL)
    // get the authors
    public List<Author> getAuthors() {
        return authors;
    }

    @Column(name = "section")
    // get the section
    public String getSection() {
        return section;
    }

    @Column(name = "shelf")
    // get the shelf
    public String getShelf() {
        return shelf;
    }

    @Column(name = "isbn_number")
    // get the isbn number
    public String getIsbnNumber() {
        return isbnNumber;
    }

    //add a customer to the waiting list of this book
    public void addToWaitingList(Customer customer) {
        waitingList.add(customer);
    }

    @ManyToMany(cascade = CascadeType.ALL)
    // get the waiting list of this book
    public List<Customer> getWaitingList() {
        return waitingList;
    }

    @Column(name = "title")
    //get the title of this book
    public String getTitle() {
        return title;
    }

    @OneToMany(cascade = CascadeType.ALL)
    // get the copies of this book
    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    // add a new copy to this book
    public void addCopy(BookCopy bookCopy) {
        bookCopies.add(bookCopy);
    }

    @Id
    // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_book_id", nullable = false)
    // get the id of this book
    public Long getBookId() {
        return bookId;
    }

    // remove the copies of this book
    public void removeCopy(BookCopy bookCopy) {
        bookCopies.remove(bookCopy);
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setBookCopies(List<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setWaitingList(List<Customer> waitingList) {
        this.waitingList = waitingList;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

}
