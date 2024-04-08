package com.project.springbootproject.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.springbootproject.dto.BookDtoWithoutCategoryIds;
import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.dto.categorydto.CategoryDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.service.BookService;
import com.project.springbootproject.service.category.CategoryService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
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

    private CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto()
                .setName("Test Category")
                .setDescription("Test Description");
        return categoryDto;
    }

    private CategoryDto getCategoryDto(String name, String description) {
        CategoryDto categoryDto = new CategoryDto()
                .setName(name)
                .setDescription(description);
        return categoryDto;
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create category")
    void createCategory() throws Exception {
        // Given
        CategoryDto categoryDto = getCategoryDto();
        CategoryDto expected = new CategoryDto()
                .setId(categoryDto.getId())
                .setName(categoryDto.getName())
                .setDescription(categoryDto.getDescription());
        String jsonRequest = objectMapper.writeValueAsString(categoryDto);

        // When
        MvcResult result = mockMvc.perform(
                        post("/category")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        // Then
        CategoryDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");

    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Find all categories")
    void findAllCategories() throws Exception {
        // Given
        List<CategoryDto> expectedCategories = new ArrayList<>();
        expectedCategories.add(getCategoryDto("Category1", "Description1"));
        expectedCategories.add(getCategoryDto("Category2", "Description2"));

        for (CategoryDto dto : expectedCategories) {
            categoryService.save(dto);
        }

        // When
        MvcResult result = mockMvc.perform(get("/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CategoryDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals("Category1", actual[0].getName());
        Assertions.assertEquals("Category2", actual[1].getName());
        Assertions.assertEquals("Description1", actual[0].getDescription());
        Assertions.assertEquals("Description2", actual[1].getDescription());
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get category by ID")
    void getCategoryById() throws Exception {
        // Given

        CategoryDto expectedCategory = getCategoryDto();
        CategoryDto save = categoryService.save(expectedCategory);

        // When
        MvcResult result = mockMvc.perform(get("/category/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        CategoryDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertEquals(expectedCategory.getName(), actual.getName());
        Assertions.assertEquals(expectedCategory.getDescription(), actual.getDescription());

    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update category by ID")
    void updateCategoryById() throws Exception {

        CategoryDto updatedCategory = getCategoryDto();
        CategoryDto save = categoryService.save(updatedCategory);

        // When/Then
        mockMvc.perform(put("/category/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/category/{id}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(save.getId()))
                .andExpect(jsonPath("$.name").value(updatedCategory.getName()))
                .andExpect(jsonPath("$.description").value(updatedCategory.getDescription()));
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete category by ID")
    public void deleteCategory_AdminRole_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryDto categoryToDel = getCategoryDto();
        CategoryDto save = categoryService.save(categoryToDel);

        // When
        mockMvc.perform(delete("/category/{id}", save.getId()))
                .andExpect(status().isNoContent());
        //Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(save.getId());
        });
    }

    @WithMockUser(username = "adminF@ukr.net", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get Books By category ID")
    public void getBooksByCategory_Success() throws Exception {
        // Given
        CategoryDto categoryToSave = getCategoryDto();
        categoryService.save(categoryToSave);
        Long categoryIdd = 1L;

        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle("Sample Book");
        bookRequestDto.setAuthor("Sample Author");
        bookRequestDto.setIsbn("1234567890");
        bookRequestDto.setPrice(BigDecimal.valueOf(19.99));
        bookRequestDto.setCategoryIds(Collections.singletonList(categoryIdd));
        bookService.save(bookRequestDto);

        // When
        Long categoryId = 1L;
        MvcResult result = mockMvc.perform(get("/category/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();
        List<BookDtoWithoutCategoryIds> books = objectMapper.readValue(content,
                new TypeReference<>() {
                });
        Assertions.assertNotNull(books);
        Assertions.assertFalse(books.isEmpty());

        for (BookDtoWithoutCategoryIds book : books) {
            Assertions.assertEquals("Sample Book", book.getTitle());
            Assertions.assertEquals("Sample Author", book.getAuthor());
            Assertions.assertEquals("1234567890", book.getIsbn());
            Assertions.assertEquals(BigDecimal.valueOf(19.99), book.getPrice());
        }
    }
}
