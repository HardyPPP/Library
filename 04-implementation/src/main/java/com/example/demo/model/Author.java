package com.example.demo.model;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "author")
public class Author {
    // this class is mainly in charge of
    // storing the basic information of authors

    private Long authorId;
    // author id

    private List<Book> books = new ArrayList<>();
    // books written by this author

    private String name;
    // author name

    public Author(String name) {
        this.name = name;
    }

    public Author() {

    }

    @ManyToMany(mappedBy = "authors")
    // get the books written by this author
    public List<Book> getBooks() {
        return books;
    }

    @Column(name = "name")
    // get the name of this author
    public String getName() {
        return name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // primary key
    @Column(name = "author_id", nullable = false)
    // get the id of the author
    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    public void setName(String name) {
        this.name = name;
    }
}
