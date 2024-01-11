package com.project.springbootproject.controller;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.dto.BookSearchParameters;
import com.project.springbootproject.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books controller",
        description = "Endpoint for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
@Transactional
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by Id",
            description = "Get book from DB by ID")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PostMapping
    @Operation(summary = "Add book in DB",
            description = "Add book in DB")
    public BookDto save(@Valid @RequestBody BookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book from DB",
            description = "Delete book from Db")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update information about book",
            description = "Update information about book")
    public void updateBook(@PathVariable Long id, @RequestBody BookRequestDto bookDto) {
        bookService.updateBook(bookDto, id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search book with parameters",
            description = "Search books with parameters")
    public List<BookDto> search(BookSearchParameters bookSearchParameters) {
        return bookService.search(bookSearchParameters);
    }
}
