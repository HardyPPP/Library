package com.example.demo.system;


import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.model.Customer;

public class ErrorDetails {
    public ErrorType errorType;
    public String msg;
    public Author author;
    public Book book;
    public Customer customer;

    public ErrorDetails(){

    }

    public ErrorDetails(ErrorType errorType, String msg, Book book) {
        this.errorType = errorType;
        this.msg = msg;
        this.book = book;
    }

    public ErrorDetails(ErrorType errorType, String msg, Author author) {
        this.errorType = errorType;
        this.msg = msg;
        this.author = author;
    }

    public ErrorDetails(ErrorType errorType, String msg, Customer customer) {
        this.errorType = errorType;
        this.msg = msg;
        this.customer = customer;
    }
    public ErrorDetails(ErrorType errorType, String msg) {
        this.errorType = errorType;
        this.msg = msg;

    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getMsg() {
        return msg;
    }

    public Author getAuthor() {
        return author;
    }

    public Book getBook() {
        return book;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "errorType=" + errorType +
                ", msg='" + msg + '\'' +
                ", author=" + author +
                ", book=" + book +
                ", customer=" + customer +
                '}';
    }
}
