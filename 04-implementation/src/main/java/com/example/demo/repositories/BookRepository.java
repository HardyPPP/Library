package com.example.demo.repositories;


import com.example.demo.model.Author;
import com.example.demo.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book,Long> {
     Book findByIsbnNumber(String isbn);
     Book findByBookId(Long bookId);
     List<Book> findAllByTitleContaining(String title);
     List<Book>findAllByAuthors(Author author);
}
