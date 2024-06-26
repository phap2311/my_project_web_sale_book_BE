package com.example.book_storemanagement.controller;

import com.example.book_storemanagement.config.service.JwtService;
import com.example.book_storemanagement.model.entity.Books;
import com.example.book_storemanagement.model.entity.Category;
import com.example.book_storemanagement.repository.IAccountRepository;
import com.example.book_storemanagement.service.account.IAccountService;
import com.example.book_storemanagement.service.book.IBookService;
import com.example.book_storemanagement.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private IBookService iBookService;
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private IAccountRepository iAccountRepository;

    @ModelAttribute("/category")
    public List<Category> findAllCategory() {
        return iCategoryService.findAll();
    }

    @GetMapping()
    public ResponseEntity<List<Books>> findAllBook() {
        List<Books> booksList = iBookService.findAll();
        return new ResponseEntity<>(booksList, HttpStatus.OK);
    }

    @GetMapping("/bookList/{accountId}")
    public ResponseEntity<List<Books>> getAllBookByAccountId(@PathVariable Long accountId, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        String account = jwtService.getUsernameFromJwtToken(token);
        iAccountRepository.findByUsername(account);
        List<Books> books = iBookService.findAllBookByAccountId(accountId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Books> getDetailBook(@PathVariable Long id) {
        Optional<Books> booksOptional = iBookService.findById(id);
        if (!booksOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booksOptional.get(), HttpStatus.OK);
    }

    @PostMapping("/createBook")
    public ResponseEntity<Books> addBook(@RequestBody Books books, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        String account = jwtService.getUsernameFromJwtToken(token);
        books.setAccount(iAccountRepository.findByUsername(account));
        Books books1 = iBookService.save(books);
        return new ResponseEntity<>(books1, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Books> updateBooks(@RequestBody Books books, @PathVariable Long id, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        String account = jwtService.getUsernameFromJwtToken(token);
        Optional<Books> bookOptional = iBookService.findById(id);
        if (!bookOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        books.setId(bookOptional.get().getId());
        books.setAccount(iAccountRepository.findByUsername(account));
        Books updateBook = iBookService.save(books);
        return new ResponseEntity<>(updateBook, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Books> deleteBook(@PathVariable Long id) {
        Optional<Books> booksOptional = iBookService.findById(id);
        if (!booksOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        iBookService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/nameBook/")
    public ResponseEntity<List<Books>> getAllBookByName(@RequestHeader("Authorization") String tokenHeader, @RequestParam String name) {
        String token = tokenHeader.substring(7);
        String account = jwtService.getUsernameFromJwtToken(token);
        iAccountRepository.findByUsername(account);
        List<Books> books = iBookService.findAllBookByName(name);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

}
