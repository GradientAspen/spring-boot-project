package com.project.springbootproject.service;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.CreateBookRequestDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.BookMapper;
import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.toModel(createBookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can not find book with id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void updateBook(BookDto bookDto) {
        Optional<Book> book = bookRepository.findById(bookDto.getId());
        bookMapper.updateBookFromBookDto(bookDto, book);
        bookRepository.save(book.orElseThrow(
                () -> new EntityNotFoundException("Can not update Book")));
    }

}
