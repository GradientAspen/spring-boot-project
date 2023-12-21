package com.project.springbootproject.service;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<BookDto> findAll();
}
