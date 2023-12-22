package com.project.springbootproject.controller;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.CreateBookRequestDto;
import com.project.springbootproject.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookbyId(id);
    }

    @PostMapping
    public BookDto save(@RequestBody CreateBookRequestDto createBookRequestDto) {
        return bookService.save(createBookRequestDto);
    }
}
