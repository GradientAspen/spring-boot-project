package com.project.springbootproject.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.dto.BookSearchParameters;
import com.project.springbootproject.service.BookService;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    ///@Autowired
    @MockBean
    private BookService bookService;

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
    @Sql(scripts = "classpath:database/books/add-three-default-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all books")
    void getAllBooks_ShouldReturnAllBooks() throws Exception {

        //Given
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto().setId(1L).setTitle("World")
                .setAuthor("Ivan Goncharenko").setIsbn("isbn11")
                .setPrice(BigDecimal.valueOf(1300.12)).setCategoryIds(Collections.emptySet()));
        expected.add(new BookDto().setId(2L).setTitle("Oceans")
                .setAuthor("Ivan Goncharenko").setIsbn("isbn12")
                .setPrice(BigDecimal.valueOf(1700.12)).setCategoryIds(Collections.emptySet()));
        expected.add(new BookDto().setId(3L).setTitle("Forests")
                .setAuthor("Semen Petrenko").setIsbn("isbn13")
                .setPrice(BigDecimal.valueOf(2800.12)).setCategoryIds(Collections.emptySet()));

        //When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get books bu Id")
    void getBookById_ShouldReturnBook_Ok() throws Exception {
        //Given
        Long id = 1L;
        BookDto expectedBookDto = new BookDto()
                .setId(id)
                .setTitle("Sample Book")
                .setAuthor("Sample Author")
                .setIsbn("1234567890")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCategoryIds(new HashSet<>());
        when(bookService.findBookById(id)).thenReturn(expectedBookDto);

        //When
        MvcResult result = mockMvc.perform(get("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expectedBookDto, actual);
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book by ID")
    void deleteBookById_AdminRole_Success() throws Exception {
        // Given
        Long id = 1L;

        // When
        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isNoContent());

        // Then
        verify(bookService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Delete book by ID - Unauthorized")
    void deleteBookById_NoUser_Unauthorized() throws Exception {
        // Given
        Long id = 1L;

        // When
        mockMvc.perform(delete("/books/{id}", id))
                .andExpect(status().isUnauthorized());

        // Then
        verify(bookService, never()).deleteById(id);
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book by ID")
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_AdminRole_Success() throws Exception {
        // Given
        Long id = 1L;
        BookRequestDto bookDto = new BookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setIsbn("Updated ISBN");
        String jsonRequest = objectMapper.writeValueAsString(bookDto);

        // When

        mockMvc.perform(put("/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)).andExpect(status().isNoContent());

        // Then
        verify(bookService, times(1)).updateBook(bookDto, id);
    }

    //   @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    //   @Test
            //   @DisplayName("Search books with parameters")
  //  void searchBooks_Success() throws Exception {
  //      // Given
  //      String[] authors = {"Author1", "Author2"};
  //      String[] descriptions = {"Description1", "Description2"};
  //      BookSearchParameters searchParameters = new BookSearchParameters(authors, descriptions);
  //      List<BookDto> expectedBooks = new ArrayList<>();
  //      // Add some expected books to the list
//
  //      // Mocking the service method
  //      when(bookService.search(argThat(arg ->
  //              Arrays.equals(arg.authors(), authors) &&
  //                      (arg.description() == null || Arrays.equals(arg.description(), descriptions))
  //      ))).thenReturn(expectedBooks);
//
  //      // When/Then
  //      MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books/search")
  //                      .contentType(MediaType.APPLICATION_JSON)
  //                      .param("authors", authors)
  //                      .param("descriptions", descriptions)) // Ensure descriptions is passed only if it's not null
  //              .andExpect(MockMvcResultMatchers.status().isOk())
  //              .andReturn();
//
  //      // Convert response to list of BookDto
  //      List<BookDto> actualBooks = objectMapper.readValue(
  //              result.getResponse().getContentAsString(),
  //              new TypeReference<List<BookDto>>() {}
  //      );
//
  //      // Verify that service method was called with correct parameters
  //      verify(bookService, times(1)).search(searchParameters);
//
  //      // Assert expected and actual results
  //      assertEquals(expectedBooks, actualBooks);
  //  }

}