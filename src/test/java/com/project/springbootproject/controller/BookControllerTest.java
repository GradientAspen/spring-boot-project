package com.project.springbootproject.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.dto.BookSearchParameters;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.book.BookRepository;
import com.project.springbootproject.service.BookService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContest
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContest)
                .apply(springSecurity())
                .build();

    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-math-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        //Given
        BookRequestDto bookRequestDto = new BookRequestDto()
                .setTitle("Math")
                .setAuthor("Semen Semenchenko")
                .setIsbn("isbn123")
                .setPrice(BigDecimal.valueOf(2500));
        BookDto expected = new BookDto()
                .setTitle(bookRequestDto.getTitle())
                .setAuthor(bookRequestDto.getAuthor())
                .setIsbn(bookRequestDto.getIsbn())
                .setPrice(bookRequestDto.getPrice());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        //When
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();

        //Then

        BookDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all books")
    void getAllBooks_ShouldReturnAllBooks() throws Exception {

        //Given
        List<BookRequestDto> expected = new ArrayList<>();
        expected.add(new BookRequestDto().setId(1L).setTitle("World")
                .setAuthor("Ivan Goncharenko").setIsbn("isbn11")
                .setPrice(BigDecimal.valueOf(1300.12)).setCategoryIds(Collections.emptyList()));
        expected.add(new BookRequestDto().setId(2L).setTitle("Oceans")
                .setAuthor("Ivan Goncharenko").setIsbn("isbn12")
                .setPrice(BigDecimal.valueOf(1700.12)).setCategoryIds(Collections.emptyList()));
        expected.add(new BookRequestDto().setId(3L).setTitle("Forests")
                .setAuthor("Semen Petrenko").setIsbn("isbn13")
                .setPrice(BigDecimal.valueOf(2800.12)).setCategoryIds(Collections.emptyList()));
        for (BookRequestDto dto:expected) {
            bookService.save(dto);
        }

        //When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookRequestDto[] actual = objectMapper.readValue(result.getResponse()
                        .getContentAsByteArray(),
                BookRequestDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get books bu Id")
    void getBookById_ShouldReturnBook_Ok() throws Exception {
        // Given
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle("Sample Book");
        bookRequestDto.setAuthor("Sample Author");
        bookRequestDto.setIsbn("1234567890");
        bookRequestDto.setPrice(BigDecimal.valueOf(19.99));
        bookRequestDto.setCategoryIds(new ArrayList<>());

        BookDto save = bookService.save(bookRequestDto);

        // When
        MvcResult result = mockMvc.perform(get("/books/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertEquals(bookRequestDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(bookRequestDto.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(bookRequestDto.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(bookRequestDto.getPrice(), actual.getPrice());
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book by ID")
    void deleteBookById_AdminRole_Success() throws Exception {
        // Given
        BookRequestDto bookToDeleteDto = new BookRequestDto()
                .setId(5L)
                .setTitle("Sample Book")
                .setAuthor("Sample Author")
                .setIsbn("1234567890")
                .setPrice(BigDecimal.valueOf(19.99));

        BookDto savedBook = bookService.save(bookToDeleteDto);

        // When
        mockMvc.perform(delete("/books/{id}", savedBook.getId()))
                .andExpect(status().isNoContent());

        // Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            // Check if the book exists in the database using the service
            bookService.findBookById(savedBook.getId());
        });
    }

    @Test
    @WithAnonymousUser
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book by ID - Unauthorized")
    void deleteBookById_NoUser_Unauthorized() throws Exception {
        // Given
        Book bookToDelete = new Book();
        bookToDelete.setTitle("Sample Book");
        bookToDelete.setAuthor("Sample Author");
        bookToDelete.setIsbn("1234567890");
        bookToDelete.setPrice(BigDecimal.valueOf(19.99));
        bookRepository.save(bookToDelete);

        Long id = bookToDelete.getId();

        // When
        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isUnauthorized());

        // Then
        Optional<Book> optionalBook = bookRepository.findById(id);
        assertTrue(optionalBook.isPresent());
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update book by ID")
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_AdminRole_Success() throws Exception {
        // Given
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Sample Author");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(19.99));
        bookRepository.save(book);

        Long id = book.getId();

        BookRequestDto bookDto = new BookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setIsbn("Updated ISBN");
        String jsonRequest = objectMapper.writeValueAsString(bookDto);

        // When
        mockMvc.perform(put("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());

        // Then
        Book updatedBook = bookRepository.findById(id)
                .orElseThrow(() -> new AssertionError("Book not found"));

        Assertions.assertEquals("Updated Title", updatedBook.getTitle());
        Assertions.assertEquals("Updated Author", updatedBook.getAuthor());
        Assertions.assertEquals("Updated ISBN", updatedBook.getIsbn());
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search books with parameters")
    void searchBooksByParameters() throws Exception {
        // Given
        String[] authors = {"Author1", "Author2"};
        String[] descriptions = {"Description1", "Description2"};
        BookSearchParameters searchParameters = new BookSearchParameters(authors, descriptions);

        BookRequestDto book1RequestDto = new BookRequestDto()
                .setAuthor("Author1")
                .setDescription("Description1")
                .setTitle("Title1")
                .setIsbn("ISBN1")
                .setPrice(BigDecimal.valueOf(15.78));
        BookRequestDto book2RequestDto = new BookRequestDto()
                .setAuthor("Author2")
                .setDescription("Description2")
                .setTitle("Title2")
                .setIsbn("ISBN2")
                .setPrice(BigDecimal.valueOf(20.15));
        bookService.save(book1RequestDto);
        bookService.save(book2RequestDto);

        // When
        mockMvc.perform(get("/books/search")
                        .param("authors", authors)
                        .param("descriptions", descriptions))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[1].author").value("Author2"))
                .andExpect(jsonPath("$[1].description").value("Description2"));
    }

}
