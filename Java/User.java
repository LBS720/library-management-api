package com.example.library;

import java.util.List;

public class User {
    private int id;
    private String name;
    private List<Integer> borrowedBooks;

    public User() {}

    public User(int id, String name, List<Integer> borrowedBooks) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = borrowedBooks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Integer> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
