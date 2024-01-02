package com.project.springbootproject.service;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.CreateBookRequestDto;

import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    void deleteById(Long id);

    void updateBook(BookDto bookDto);

}
