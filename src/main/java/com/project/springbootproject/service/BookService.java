package com.project.springbootproject.service;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.dto.BookSearchParameters;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookDto save(BookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    void deleteById(Long id);

    void updateBook(BookRequestDto bookDto, Long id);

    List<BookDto> search(BookSearchParameters params);

}
