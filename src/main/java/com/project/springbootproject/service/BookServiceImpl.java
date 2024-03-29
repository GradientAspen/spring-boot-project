package com.project.springbootproject.service;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.BookDtoWithoutCategoryIds;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.dto.BookSearchParameters;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.BookMapper;
import com.project.springbootproject.model.Book;
import com.project.springbootproject.model.Category;
import com.project.springbootproject.repository.book.BookRepository;
import com.project.springbootproject.repository.book.BookSpecificationBuilder;
import com.project.springbootproject.repository.category.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(BookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        Set<Category> categoriesByIdIn = categoryRepository.findCategoriesByIdIn(bookRequestDto
                .getCategoryIds());
        book.setCategories(categoriesByIdIn);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(String email, Pageable pageable) {
        return bookRepository.findAll(pageable)
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
    public void updateBook(BookRequestDto bookDto, Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book bookToUpdate = optionalBook.orElseThrow(
                () -> new EntityNotFoundException("Can not find Book with ID: " + id));

        bookMapper.updateBookFromBookDto(bookDto, bookToUpdate);
        bookRepository.save(bookToUpdate);
    }

    @Override
    public List<BookDto> search(BookSearchParameters params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId) {
        List<BookDtoWithoutCategoryIds> bookDtos = bookRepository.findAllByCategoriesId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .collect(Collectors.toList());
        return bookDtos;
    }

}
