package com.example.demo.services;


import com.example.demo.model.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class LibraryService {

    private final AuthorRepository authors;
    private final BookCopyRepository bookCopies;
    private final BookRentalRepository bookRentals;
    private final BookRepository books;
    private final CustomerRepository customers;


    // The constructor
    @Autowired
    public LibraryService(AuthorRepository authorRepository, BookCopyRepository bookCopies, BookRentalRepository bookRentals, BookRepository bookRepository, CustomerRepository customerRepository) {
        this.authors = authorRepository;
        this.bookCopies = bookCopies;
        this.bookRentals = bookRentals;
        this.books = bookRepository;
        this.customers = customerRepository;
    }

    public Author getAuthor(String name) {

        if (authors.findByName(name) != null) {
            // Find the Author by name.
            return authors.findByName(name);
        } else {
            // If no corresponding object is found,
            // an Author is created with that name and stored in the database.
            if (!name.trim().equals("")) {
                // if the author name is not empty
                Author author = new Author(name);
                authors.save(author);
                // save the author
                return author;
            } else {
                return null;
            }
        }
    }

    public List<Author> getAllAuthor(String name) {
        return authors.findAllByNameContaining(name);
    }

    public List<BookCopy> getAllCopies(String text){
        return bookCopies.findAll();
    }


    // This method looks through the isbn to see if there are duplicate books.
    public Book addBook(String title, String authors, String isbn, String section, String shelf) {
        if (!title.trim().equals("") && !authors.trim().equals("") && !isbn.trim().equals("") && !section.trim().equals("") && !shelf.trim().equals("")) {
            // if all the input area is filled
            if (books.findByIsbnNumber(isbn) != null) {
                // if the book is already in the system
                return books.findByIsbnNumber(isbn);
            } else {
                // If it does not exist, the book is added to the database by entering the information.
                String[] AuthorListTemp = authors.split(",");
                // Handling the authors: the String containing all author names is first separated by commas.
                // Then getAuthor is called to find the corresponding author and add the author to the list.
                List<Author> authorList = new ArrayList<>();
                for (String s : AuthorListTemp) {
                    // for each author you want to add
                    Author singleAuthor = getAuthor(s);
                    // find the author if he exists or create a new one
                    if (singleAuthor != null) {
                        authorList.add(singleAuthor);
                    }
                }
                Book book = new Book(title, isbn, authorList, section, shelf);
                books.save(book);
                // save the book
                return book;
            }
        } else {
            return null;
        }
    }



    public Customer addCustomer(String name, LocalDate dateOfBirth, String email, String address, Integer maxRentals, String phone_number) {
        // Similar to adding books. This method will first find the duplicate customer in the database,
        if (!name.trim().equals("") && dateOfBirth != null && !email.trim().equals("") && !address.trim().equals("") && !phone_number.trim().equals("")) {
            // if all the input areas are filled
            Customer customerTemp = customers.findByEmail(email);
            if (customerTemp == null) {
                // if not, the customer and customer information will be added to the database.
                Customer customer = new Customer(name, email, address, dateOfBirth, maxRentals, phone_number);
                customers.save(customer);
                // save the author
                return customer;
            }
            return customerTemp;
        } else {
            return null;
        }

    }

    //Determines whether a number exists in the String
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile(".*[0-9].*");
        return pattern.matcher(str).matches();
    }

    // Find the book and the customer first, and then add the customer to the waitingList of books.
    public Book addToWaitingList(Long bookId, Long customerId) {
        Book book = books.findByBookId(bookId);
        Customer customer = customers.findByCustomerId(customerId);
        // find the book and customer
        if (customer == null) {
            return null;
        } else {
            // Finally save the state of the book.
            book.addToWaitingList(customer);
            customers.save(customer);
            books.save(book);
            return book;
        }
    }


    public Customer getCustomer(Long id) {
        return customers.findByCustomerId(id);
    }



    public List<Customer> findCustomer(String text) {
        List<Customer> customerList = new ArrayList<>();
        //The method first determines whether the input data is the customer Id by whether there is a number in the input data.
        if (isNumeric(text)) {
            // if the text is number, then search by id
            Customer customer = customers.findByCustomerId(Long.valueOf(text));
            customerList.add(customer);
            return customerList;
        } else {
            // If not, the data will be used to determine whether it is an Email, Name or Address, and the data will be used to search customer.
            Customer customerByEmail = customers.findByEmail(text);
            List<Customer> customersByName = customers.findAllByNameContaining(text);
            List<Customer> customersByAddress = customers.findAllByAddress(text);
            if (customerByEmail != null) {
                customerList.add(customerByEmail);
            }
            customerList.addAll(customersByName);
            customerList.addAll(customersByAddress);
            return customerList;

        }
    }

    //This method is used for renting books
    public Customer addRentals(Long customerId, Long bookCopyId, LocalDate checkoutDate, LocalDate returnDate) {
        //Determine if the return date is before the loan date
        if (returnDate.isAfter(checkoutDate)) {
            // the return date should be after check out date
            Customer customer = customers.findByCustomerId(customerId);
            //Determine if the maximum number of books borrowed by the user is exceeded
            if (customer.CurrentRentals() < customer.getMaxRental()) {
                BookCopy bookCopy = bookCopies.findByBookCopyId(bookCopyId);
                //If the conditions are met then the rent book operation is performed
                if (bookCopy != null) {
                    // if the copy is found
                    bookCopy.setExpectedReturnDate(returnDate);
                    // set return date
                    bookCopy.setAvailable(false);
                    // make it not available
                    bookCopies.save(bookCopy);
                    BookRental bookRental = new BookRental(checkoutDate, returnDate, bookCopy, customer);
                    bookRentals.save(bookRental);
                    customer.addRental(bookRental);
                    // add this rental to the customer
                    customers.save(customer);
                    return customer;
                } else {
                    return null;
                }
            } else {
                return customer;
            }
        }else {
            return null;
        }
    }

    // This method determines whether the customer and the BookCopy exists.
    public Customer addReturn(Long customerId, Long bookCopyId, LocalDate returnDate) {
        Customer customer = customers.findByCustomerId(customerId);
        BookCopy bookCopy = bookCopies.findByBookCopyId(bookCopyId);
        if (customer != null && bookCopy != null) {
            // If both exist, the state of the BookCopy is changed first (such as setting the actual return time,
            // as well as the loanable state). The bookRentals status is then set to returned as well.
            List<BookRental> newRental = bookRentals.findByCustomerAndBookCopy(customer, bookCopy);
            bookCopy.setAvailable(true);
            bookCopy.setExpectedReturnDate(null);
            bookCopies.save(bookCopy);
            for (BookRental bookRental : newRental) {
                if (bookRental.getStatus() == RentalStatus.RENTED) {
                    // find the rental that is not returned
                    bookRental.setStatus(RentalStatus.RETURNED);
                    // set the rental status to returned
                    bookRental.setActualReturnDate(returnDate);
                    //set the return date
                    // Finally determine whether overdue and set LateFees
                    if (returnDate.isAfter(bookRental.getExpectedReturnDate())) {
                        // if the return date is after the expect return date
                        bookRental.setLateFees(12);
                        // set a late fee to the customer
                        customer.setLateFees(customer.getLateFees() + 12);
                    }
                    bookRentals.save(bookRental);
                    customers.save(customer);
                }
            }
            customers.save(customer);
            return customer;
        } else {
            return customer;
        }

    }

    //This method calculates the amount delivered by the customer and finally recalculates the amount required to be delivered by the customer
    public Customer addPayment(Long customerId, int paymentAmount) {
        Customer customer = getCustomer(customerId);
        // get the customer
        if (customer.getLateFees() - paymentAmount < 0) {
            // if the payment amount is larger than the late fee
            paymentAmount = customer.getLateFees();
        }
        customer.setLateFees(customer.getLateFees() - paymentAmount);
        // update the late fee
        customers.save(customer);
        return customer;
    }

    public List<BookRental> findAll(String s) {
        // find all book rental
        return bookRentals.findAll();
    }

    //This method adds BookCopy to the database and calls the addCopy method in book
    public List<Long> addBookCopies(long bookId, int copyNum) {
        Book book = books.findByBookId(bookId);
        if(book == null){
            return null;
        }else{
            // if the book is found
            List<Long> idList = new ArrayList<>();
            for (int i = 0; i < copyNum; i++) {
                // create multiple copies
                BookCopy newBookCopy = new BookCopy(book);
                bookCopies.save(newBookCopy);
                book.addCopy(newBookCopy);
                long bookCopyId = newBookCopy.getBookCopyId();
                // gather the id of  new copies into a list
                idList.add(bookCopyId);
            }
            return idList;
        }
    }


    public Book getBook(Long id) {
        return books.findByBookId(id);
    }

    public BookCopy findBookCopy(Long bookCopyId){
        return bookCopies.findByBookCopyId(bookCopyId);
    }

    public List<BookCopy> findAllBookCopy(){
        return bookCopies.findAll();
    }

    //This method is used to remove the Book Copy
    public void removeBookCopy(Long bookCopyId) {
        //The data of the book and the data of the book copy are removed
        BookCopy bookCopy = bookCopies.findByBookCopyId(bookCopyId);
        List<BookRental> br = bookRentals.findByBookCopy(bookCopy);
        // find the rental records that contain this copy
        Book book = bookCopy.getBook();
        book.removeCopy(bookCopy);
        // remove the copies from the original book
        for (BookRental b : br) {
            Customer c = b.getCustomer();
            c.getBookRentals().remove(b);
            // remove the copies from the customer rental record
            customers.save(c);
        }
        bookRentals.deleteAll(br);
        // delete the rental records
        bookCopies.delete(bookCopy);
        // delete the book copies
        books.save(book);
    }

    public List<Book> findBook(String text) {
        List<Book> bookList = new ArrayList<Book>();
        // The method first determines whether the input data is the Book Id by whether there is a number in the input data.
        if (isNumeric(text)) {
            // If it is, it looks up the Book by that Id.
            Book book = books.findByBookId(Long.valueOf(text));
            bookList.add(book);
            return bookList;
        } else {
            // If not, the data will be used to determine whether it is an Title or authorName, and the data will be used to search books.
            Book book1 = books.findByIsbnNumber(text);
            if (book1 != null) {
                bookList.add(book1);
            }
            List<Book> bookList2 = books.findAllByTitleContaining(text);
            bookList.addAll(bookList2);
            Author authorTarget = authors.findByName(text);
            List<Book> authorBooks = books.findAllByAuthors(authorTarget);
            bookList.addAll(authorBooks);
            return bookList;
        }
    }
    public List<Customer> findWhoHasLateFee(){
        return customers.findAllByLateFeesGreaterThan(0);
    }

    public List<BookRental> findCurrentRentedRentals(){
        return bookRentals.findAllByStatus(RentalStatus.RENTED);
    }

    public List<BookRental> findLateRentals(LocalDate date){
        List<BookRental> brs = findCurrentRentedRentals();
        List<BookRental> result = new ArrayList<BookRental>();
        for (BookRental br : brs){
            if ((br.getExpectedReturnDate()).isBefore(date)){
                result.add(br);
            }
        }
        return result;
    }

    public boolean isCopyAvailable(Long bid) {
        // find if the copy is available
        if (bookCopies.findByBookCopyId(bid)!=null) {
            return bookCopies.findByBookCopyId(bid).getAvailable();
        }else {
            return false;
        }
    }

    public boolean isCustomerExist(Long bid) {
        // find if the customer exist
        return customers.findByCustomerId(bid) != null;
    }

    public boolean isBookExist(Long bid) {
        // find if the book exist
        return books.findByBookId(bid) != null;
    }

    public boolean isBookCopyExist(Long bid) {
        // find if the copy exist
        return bookCopies.findByBookCopyId(bid) != null;
    }

    public boolean isMaxRental(Long cid) {
        // find if the customer reach max rental
        Customer customer = customers.findByCustomerId(cid);
        return customer.CurrentRentals() >= customer.getMaxRental();
    }
}
