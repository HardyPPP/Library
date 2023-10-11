package com.example.demo.repositories;


import com.example.demo.model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy,Long> {
     BookCopy findByBookCopyId(Long id);
}
