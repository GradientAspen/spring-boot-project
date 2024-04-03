package com.project.springbootproject.service;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.BookDtoWithoutCategoryIds;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.BookMapper;
import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.book.BookRepository;
import com.project.springbootproject.repository.book.BookSpecificationBuilder;
import com.project.springbootproject.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Save book in DB")
    void save_ValidBookRequestDto_ReturnsSavedBookDto() {
        // Given
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle("Test Title");
        bookRequestDto.setAuthor("Test Author");
        bookRequestDto.setIsbn("Test ISBN");
        bookRequestDto.setPrice(BigDecimal.valueOf(10.99));
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(1L);
        categoryIds.add(2L);
        bookRequestDto.setCategoryIds(categoryIds);

        Set<Long> categoryIdsSet = new HashSet<>(categoryIds);

        when(categoryRepository.findCategoriesByIdIn(new ArrayList<>(categoryIdsSet))).thenReturn(new HashSet<>());

        Book book = new Book();
        book.setId(1L);
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Test Book");
        when(bookRepository.save(book)).thenReturn(savedBook);

        BookDto expectedBookDto = new BookDto()
                .setId(1L)
                .setTitle("Test Book")
                .setCategoryIds(categoryIdsSet);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedBookDto);

        // When
        BookDto savedBookDto = bookService.save(bookRequestDto);

        // Then
        assertNotNull(savedBookDto);
        assertEquals(1L, savedBookDto.getId());
        assertEquals("Test Book", savedBookDto.getTitle());
        assertNotNull(savedBookDto.getCategoryIds());
        assertEquals(2, savedBookDto.getCategoryIds().size());
        assertTrue(savedBookDto.getCategoryIds().contains(1L));
        assertTrue(savedBookDto.getCategoryIds().contains(2L));
    }

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find all books")
    void findAll_ReturnsListOfBookDto() {
        // Given
        Pageable pageable = Pageable.unpaged();
        List<Book> books = new ArrayList<>();
        books.add(new Book().setId(1L)
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setIsbn("ISBN 1")
                .setPrice(BigDecimal.valueOf(10.99)));
        books.add(new Book().setId(2L)
                .setTitle("Book 2")
                .setAuthor("Author 2")
                .setIsbn("ISBN 2")
                .setPrice(BigDecimal.valueOf(15.99)));

        Page<Book> bookPage = new PageImpl<>(books);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        List<BookDto> expectedBookDtos = new ArrayList<>();
        expectedBookDtos.add(new BookDto().setId(1L)
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setIsbn("ISBN 1")
                .setPrice(BigDecimal.valueOf(10.99)));
        expectedBookDtos.add(new BookDto().setId(2L)
                .setTitle("Book 2")
                .setAuthor("Author 2")
                .setIsbn("ISBN 2")
                .setPrice(BigDecimal.valueOf(15.99)));

        when(bookMapper.toDto(any())).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            return expectedBookDtos.stream()
                    .filter(dto -> dto.getId().equals(book.getId()))
                    .findFirst()
                    .orElse(null);
        });

        // When
        List<BookDto> foundBookDtos = bookService.findAll("test@example.com", pageable);

        // Then
        assertNotNull(foundBookDtos);
        assertEquals(2, foundBookDtos.size());
        assertEquals(expectedBookDtos.get(0), foundBookDtos.get(0));
        assertEquals(expectedBookDtos.get(1), foundBookDtos.get(1));
    }

    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find books by Id")
    void findBookById_ExistingId_ReturnsBookDto() {
        // Given
        Long bookId = 1L;
        Book book = new Book().setId(bookId)
                .setTitle("Test book")
                .setAuthor("Test Author")
                .setIsbn("Test ISBN")
                .setPrice(BigDecimal.valueOf(19.99));
        BookDto expectedBookDto = new BookDto()
                .setId(bookId)
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("Test ISBN")
                .setPrice(BigDecimal.valueOf(19.99));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        // When
        BookDto foundBookDto = bookService.findBookById(bookId);

        // Then
        assertNotNull(foundBookDto);
        assertEquals(expectedBookDto, foundBookDto);
    }

    @Test
    @DisplayName("Find books by Id throw Exception")
    void findBookById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long nonExistingId = 999L;
        when(bookRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(nonExistingId));
    }

    @Test
    @DisplayName("Delete book by Id ")
    void deleteById_ValidId_DeletesBook() {
        // Given
        Long bookId = 1L;

        // When
        bookService.deleteById(bookId);

        // Then
        verify(bookRepository, times(1)).deleteById(bookId);
    }
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update Book: Existing ID")
    void updateBook_ExistingId_UpdatesBook() {
        // Given
        Long bookId = 1L;
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle("Updated Title");
        bookRequestDto.setAuthor("Updated Author");
        bookRequestDto.setIsbn("Updated ISBN");
        bookRequestDto.setPrice(BigDecimal.valueOf(25.99));

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Original Title");
        existingBook.setAuthor("Original Author");
        existingBook.setIsbn("Original ISBN");
        existingBook.setPrice(BigDecimal.valueOf(19.99));

        // Stubbing repository call
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        // When
        bookService.updateBook(bookRequestDto, bookId);

        // Then
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("Update Book: Non-existing ID")
    void updateBook_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long nonExistingId = 999L;
        BookRequestDto bookRequestDto = new BookRequestDto();

        when(bookRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(bookRequestDto, nonExistingId));
        verify(bookRepository, times(1)).findById(nonExistingId);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }


    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find book by category Id")
    void findBooksByCategoryId_ReturnsListOfBookDtoWithoutCategoryIds() {
        // Given
        Long categoryId = 1L;
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN 1");
        book1.setPrice(BigDecimal.valueOf(10.99));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN 2");
        book2.setPrice(BigDecimal.valueOf(15.99));

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(books);

        BookDtoWithoutCategoryIds bookDto1 = new BookDtoWithoutCategoryIds();
        bookDto1.setId(1L);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("ISBN 1");
        bookDto1.setPrice(BigDecimal.valueOf(10.99));

        BookDtoWithoutCategoryIds bookDto2 = new BookDtoWithoutCategoryIds();
        bookDto2.setId(2L);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("ISBN 2");
        bookDto2.setPrice(BigDecimal.valueOf(15.99));

        List<BookDtoWithoutCategoryIds> expectedBookDtos = new ArrayList<>();
        expectedBookDtos.add(bookDto1);
        expectedBookDtos.add(bookDto2);

        when(bookMapper.toDtoWithoutCategories(any())).thenReturn(bookDto1, bookDto2);

        // When
        List<BookDtoWithoutCategoryIds> foundBookDtos = bookService.findBooksByCategoryId(categoryId);

        // Then
        assertNotNull(foundBookDtos);
        assertEquals(2, foundBookDtos.size());
        assertEquals(expectedBookDtos, foundBookDtos);
    }
}