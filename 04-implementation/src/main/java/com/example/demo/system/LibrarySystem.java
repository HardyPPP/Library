package com.example.demo.system;


import com.example.demo.model.*;
import com.example.demo.services.LibraryService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LibrarySystem {
    private final LibraryService libraryService;

    @Autowired
    public LibrarySystem(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/initDB")
    //checked
    public String initDB() {
        Customer c1 = libraryService.
                addCustomer("HaiYiJin",LocalDate.of(2001,11,22),
                        "3374618571@qq.com","BJUT",10,"187222396825");
        Customer c2 = libraryService.
                addCustomer("RonyYu",LocalDate.of(2002,6,4),
                        "137742666@qq.com","BJUT",10,"13612199245");
        Author a1 = libraryService.getAuthor("HardyJin");
        Author a2 = libraryService.getAuthor("MashRoomSteven");
        Book b1 = libraryService.addBook("BadBook","HardyJin,MashRoomSteven","1234567",
                "education","1");
        Book b2 = libraryService.addBook("GoodBook","MashRoomSteven","1234568",
                "education","2");
        List<Long> copy = libraryService.addBookCopies(1,12);
        Customer c3 = libraryService.addRentals(1L,1L ,LocalDate.of(2022,12,9),LocalDate.of(2022,12,14));
        Book b3 = libraryService.addToWaitingList(1L,1L);
        return "MainPage";
    }
    @PostMapping("/addAuthor")
    public String addAuthor(
            @RequestParam(name = "authorName") String authorName,
            Model model) {
        // this function will add an author and return to the index page
        Author a = libraryService.getAuthor(authorName);
        if (a == null) {
            model.addAttribute("error", new ErrorDetails(ErrorType.EMPTY_AUTHOR, "please enter the author name"));
        }
        model.addAttribute("authorList", libraryService.getAllAuthor(""));
        return "AuthorManagement";
    }

    @PostMapping("/addBook")
    public String addBook(
            @RequestParam(name = "authorName") String authorName,
            @RequestParam(name = "bookTitle") String bookTitle,
            @RequestParam(name = "isbnNumber") String isbnNumber,
            @RequestParam(name = "section") String section,
            @RequestParam(name = "shelf") String shelf,
            Model model) {
        // this function will add a book with required field and show the book details
        Book b = libraryService.addBook(bookTitle, authorName, isbnNumber, section, shelf);
        if (b == null) {
            model.addAttribute("error", new ErrorDetails(ErrorType.EMPTY_BOOK, "All the input should be filled"));
            return "BookManagement";
        } else {
            model.addAttribute("book", b);
            return "BookShow";
        }
    }

    @PostMapping("/addCustomer")
    public String addCustomer(
            @RequestParam(name = "customerName") String customerName,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            @RequestParam(name = "dateOfBirth")  LocalDate dateOfBirth,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "address") String address,
            @RequestParam(name = "maxRentals") int maxRentals,
            @RequestParam(name = "phone_number") String phone_number,
            Model model) {
        // this function will add a customer with the required fields and show the detail of customer
        Customer c = libraryService.addCustomer(customerName, dateOfBirth, email, address, maxRentals, phone_number);
        if (c == null) {
            model.addAttribute("error", new ErrorDetails(ErrorType.EMPTY_CUSTOMER, "All the input should be filled"));
        } else {
            List<BookRental> brs = c.getBookRentals();
            model.addAttribute("customer", c);
            model.addAttribute("bookRental", brs);
        }
        return "CustomerManagement";
    }

    @PostMapping("/findBook")
    public String findBook(
            @RequestParam(name = "text") String text,
            Model model
    ) {
        // this function will return the books that match to the input information
        List<Book> book = libraryService.findBook(text);
        // in the library service, the find book will return a list contains at most
        // 3 parts, the books that match the title, the book that matches the isbn number
        // the books that match the author
        // No related book is found only if three of them are all empty
        boolean empty = true;
        for (Book b : book) {
            if (b != null) {
                empty = false;
                break;
            }
        }
        if (empty) {
            // if no book found
            model.addAttribute("error", new ErrorDetails(ErrorType.EMPTY_BOOK, "no related book found"));
        } else {
            model.addAttribute("bookList", book);
        }
        return "BookManagement";

    }

    @GetMapping("/selectBook")
    public String selectBook(
            @RequestParam(name = "bookId") Long bookId,
            Model model
    ) {
        // this function will show all the copies of this specific book
        Book book = libraryService.getBook(bookId);
        List<BookCopy> copies = book.getBookCopies();
        model.addAttribute("book", book);
        model.addAttribute("copies", copies);
        model.addAttribute("waitingList",book.getWaitingList());
        return "bookDetail";
    }

    @PostMapping("/addToWaitingList")
    public String addToWaitingList(
            @RequestParam(name = "customerId") Long customerId,
            @RequestParam(name = "bookId") Long bookId,
            Model model
    ) {
        // this function will add a customer to the waiting list of a specific book
        // and return the waiting list of this book
        Book book = libraryService.getBook(bookId);
        List<Customer> waitingList = book.getWaitingList();
        List<BookCopy> copies = book.getBookCopies();
        Book b = libraryService.addToWaitingList(bookId, customerId);
        if (b == null) {
            model.addAttribute("error", new ErrorDetails(ErrorType.CUSTOMER_NOT_EXIST, "No available customer in the repository"));
            model.addAttribute("book", book);
            model.addAttribute("waitingList", waitingList);
            return "bookDetail";
        } else {
            model.addAttribute("book", book);
            model.addAttribute("waitingList", waitingList);
            model.addAttribute("copies", copies);
            return "bookDetail";
        }
    }

    @PostMapping("/addReturn")
    public String addReturn(
            @RequestParam(name = "customerReturnId") Long cid,
            @RequestParam(name = "bookCopyReturnId") Long bid,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            @RequestParam(name = "returnDateReturn") LocalDate rd,
            Model model) {
        // this function will change the status of a rental record to returned
        // and show all the rental details of this customer
        Customer c = libraryService.addReturn(cid, bid, rd);
        List<BookRental> brs = c.getBookRentals();
        model.addAttribute("customer", c);
        model.addAttribute("bookRental", brs);
        return "BookRentalsManagement";
    }

    @PostMapping("/addPayment")
    public String addPayment(
            @RequestParam(name = "customerPayId") Long cid,
            @RequestParam(name = "paymentAmount") int pa,
            Model model) {
        // this function will accept the payment that the customer give
        // and show the customer detail and his rental records
        Customer c = libraryService.addPayment(cid, pa);
        List<BookRental> brs = c.getBookRentals();

        model.addAttribute("customer", c);
        model.addAttribute("rental", brs);
        return "CustomerDetail";
    }

    @PostMapping("/addBookCopies")
    public String addBookCopies(
            @RequestParam(name = "bookIdAdd") Long cid,
            @RequestParam(name = "numberCopies") int nc,
            Model model) {
        // this function will add a number of copies to specific book
        // and show the id list of the book's copies
        List<Long> bcs = libraryService.addBookCopies(cid, nc);
        if(bcs==null){
            model.addAttribute("error",new ErrorDetails(ErrorType.EMPTY_BOOK,"Book is not exist"));
        }else{
            Book b = libraryService.getBook(cid);
            model.addAttribute("book", b);
            model.addAttribute("idList", bcs);
        }
        return "BookCopiesManagement";
    }

    @PostMapping("/removeBookCopy")
    public String removeBookCopy(
            @RequestParam(name = "bookIdRemove") Long cid,
            Model model) {
        // this function will delete a specific copy of the specific book
        BookCopy bookCopy=libraryService.findBookCopy(cid);
        if(bookCopy==null){
            model.addAttribute("error",new ErrorDetails(ErrorType.BOOK_COPY_NOT_EXIST,"No available book copy"));
            return "BookCopiesManagement";
        }
        else {
            libraryService.removeBookCopy(cid);
            return "BookCopiesManagement";
        }
    }

    @PostMapping("/findCustomer")
    public String findCustomer(
            @RequestParam(name = "customerFindText") String text,
            Model model) {
        // this function will find the customers match the information input
        List<Customer> cs = libraryService.findCustomer(text);
        // in the library service, the findCustomer will return a list contains at most
        // 3 parts, the customers that match the name, the customer that matches the email
        // the customers that match the address
        // No related customer is found only if three of them are all empty
        boolean empty = true;
        for (Customer c : cs) {
            if (c != null) {
                empty = false;
                break;
            }
        }
        if (empty) {
            // if no customer is found
            model.addAttribute("error", new ErrorDetails(ErrorType.EMPTY_CUSTOMER, "no related customer found"));
        } else {
            model.addAttribute("customer", cs);
        }
        return "CustomerManagement";
    }


    @GetMapping("/showCustomer")
    //checked
    public String showCustomer(
            @RequestParam(name = "customerShowId") Long id,
            Model model) {
        Customer c = libraryService.getCustomer(id);
        model.addAttribute("customer", c);
        model.addAttribute("rental", c.getBookRentals());
        return "CustomerDetail";
    }


    @PostMapping("/addRental")
    public String addRental(
            @RequestParam(name = "customerRentalId") Long cid,
            @RequestParam(name = "bookCopyRentalId") Long bid,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            @RequestParam(name = "checkOutDateRental") LocalDate cod,
            @DateTimeFormat(pattern="yyyy-MM-dd")
            @RequestParam(name = "returnDateRental") LocalDate rd,
            Model model) {
        // the function will create a rental record with specific customer, book copy
        // check out date and expect return date
        if (!libraryService.isCopyAvailable(bid)) {
            // if the copy is not available
            model.addAttribute("error", new ErrorDetails(ErrorType.NOT_AVAILABLE_COPY, "not an available copy"));
        } else if (!libraryService.isBookCopyExist(bid)) {
            // if the copy is not exist
            model.addAttribute("error", new ErrorDetails(ErrorType.BOOK_COPY_NOT_EXIST, "book copy not exist"));
        } else if (!libraryService.isCustomerExist(cid)) {
            // if the customer is not exist
            model.addAttribute("error", new ErrorDetails(ErrorType.CUSTOMER_NOT_EXIST, "customer not exist"));
        } else if (libraryService.isMaxRental(cid)) {
            // if reach max rental time
            model.addAttribute("error", new ErrorDetails(ErrorType.REACH_RENTALRECORD, "reach max rental time"));
        } else {
            Customer c = libraryService.addRentals(cid, bid, cod, rd);
            if (c != null) {
                model.addAttribute("customer", c);
                model.addAttribute("rental",c.getBookRentals());
            }else {
                model.addAttribute("error",new ErrorDetails(ErrorType.BAD_DAY,"return date should be after check out date"));
            }
        }
        return "CustomerDetail";
    }

    @GetMapping("/BookManagement")
    public String returnBookManagement(Model model) {
        // book management page that shows all the books
        model.addAttribute("bookList", libraryService.findBook(""));
        return "BookManagement";
    }

    @GetMapping("/BookRentalsManagement")
    public String returnBookRentalsManagement(Model model) {
        // Book Rentals Management page that shows all the rental records
        model.addAttribute("bookRental", libraryService.findAll(""));
        return "BookRentalsManagement";
    }

    @GetMapping("/CustomerManagement")
    public String CustomerManagement(Model model) {
        // Customer Management page and show all the customers
        model.addAttribute("customer", libraryService.findCustomer(""));
        return "CustomerManagement";
    }

    @GetMapping("/BookCopiesManagement")
    public String BookCopiesManagement() {
        // Book Copies Management page
        return "BookCopiesManagement";
    }

    @GetMapping("/AuthorManagement")
    public String AuthorManagement(Model model) {
        // Book Copies Management page
        model.addAttribute("authorList", libraryService.getAllAuthor(""));
        return "AuthorManagement";
    }

    @GetMapping("/")
    public String HomePage() {
        return "MainPage";
    }

}
