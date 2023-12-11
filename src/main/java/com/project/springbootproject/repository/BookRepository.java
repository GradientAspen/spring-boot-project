package com.project.springbootproject.repository;

import com.project.springbootproject.model.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
