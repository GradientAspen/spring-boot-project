package com.project.springbootproject.repository.book;

import com.project.springbootproject.model.Book;
import com.project.springbootproject.model.Category;
import com.project.springbootproject.repository.category.CategoryRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find all books by Category")
    void findAllByCategoryId_PositiveCase() {
        Category category = new Category();
        category.setName("Programming");
        category.setDescription("Test2");

        Book book = new Book();
        book.setIsbn("isbn124555");
        book.setTitle("Math");
        book.setPrice(BigDecimal.valueOf(100));
        book.setAuthor("Petrenko Ivan");
        book.setDeleted(false);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        book.setCategories(categories);

        Category categorySave = categoryRepository.save(category);
        Book save = bookRepository.save(book);

        List<Book> actual = bookRepository.findAllByCategoriesId(categorySave.getId());
        Assertions.assertEquals(1, actual.size());
    }
}
