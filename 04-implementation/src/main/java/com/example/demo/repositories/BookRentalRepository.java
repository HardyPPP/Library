package com.example.demo.repositories;


import com.example.demo.model.BookCopy;
import com.example.demo.model.BookRental;
import com.example.demo.model.Customer;
import com.example.demo.model.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookRentalRepository extends JpaRepository<BookRental,Long> {


     List<BookRental> findByCustomerAndBookCopy(Customer customer, BookCopy bookCopy);

     List<BookRental> findByBookCopy( BookCopy bookCopy);

     List<BookRental> findAllByStatus(RentalStatus rentalStatus);
     // new requirements
     List<BookRental> findAllByStatusAndExpectedReturnDateIsBefore(RentalStatus rentalStatus, LocalDate date);
     // new requirements
}
