package com.example.library;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LibraryController {

    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    public LibraryController() {
        books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", false));
        books.add(new Book(2, "To Kill a Mockingbird", "Harper Lee", false));

        users.add(new User(1, "Alice", new ArrayList<>()));
        users.add(new User(2, "Bob", new ArrayList<>()));
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return books;
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book newBook) {
        if (newBook.getTitle() == null || newBook.getAuthor() == null) {
            throw new IllegalArgumentException("Missing required fields");
        }
        newBook.setId(books.size() + 1);
        newBook.setBorrowed(false);
        books.add(newBook);
        return newBook;
    }

    @PutMapping("/books/{bookId}")
    public Book updateBook(@PathVariable int bookId, @RequestBody Book updatedBook) {
        Optional<Book> bookOpt = books.stream().filter(book -> book.getId() == bookId).findFirst();
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }
        Book book = bookOpt.get();
        if (updatedBook.getTitle() != null) book.setTitle(updatedBook.getTitle());
        if (updatedBook.getAuthor() != null) book.setAuthor(updatedBook.getAuthor());
        book.setBorrowed(updatedBook.isBorrowed());
        return book;
    }

    @DeleteMapping("/books/{bookId}")
    public String deleteBook(@PathVariable int bookId) {
        books.removeIf(book -> book.getId() == bookId);
        return "Book deleted";
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    @PostMapping("/users/{userId}/borrow/{bookId}")
    public String borrowBook(@PathVariable int userId, @PathVariable int bookId) {
        Optional<User> userOpt = users.stream().filter(user -> user.getId() == userId).findFirst();
        Optional<Book> bookOpt = books.stream().filter(book -> book.getId() == bookId).findFirst();

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }

        User user = userOpt.get();
        Book book = bookOpt.get();

        if (book.isBorrowed()) {
            throw new IllegalArgumentException("Book already borrowed");
        }

        book.setBorrowed(true);
        user.getBorrowedBooks().add(bookId);
        return "Book borrowed successfully";
    }

    @PostMapping("/users/{userId}/return/{bookId}")
    public String returnBook(@PathVariable int userId, @PathVariable int bookId) {
        Optional<User> userOpt = users.stream().filter(user -> user.getId() == userId).findFirst();
        Optional<Book> bookOpt = books.stream().filter(book -> book.getId() == bookId).findFirst();

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found");
        }

        User user = userOpt.get();
        Book book = bookOpt.get();

        if (!user.getBorrowedBooks().contains(bookId)) {
            throw new IllegalArgumentException("Book was not borrowed by the user");
        }

        book.setBorrowed(false);
        user.getBorrowedBooks().remove(Integer.valueOf(bookId));
        return "Book returned successfully";
    }
}
