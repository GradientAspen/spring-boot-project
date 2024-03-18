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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            Find all books by Category id:10
            """)
    void findAllByCategoryId_PositiveCase() {
        Category category = new Category();
        category.setName("Programming");
        categoryRepository.save(category);
        Book book = new Book();
        book.setId(1L);
        book.setIsbn("isbn12");
        book.setTitle("Math");
        book.setPrice(BigDecimal.valueOf(100));
        book.setAuthor("Petrenko Ivan");
        book.setDeleted(false);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        book.setCategories(categories);

        bookRepository.save(book);
        List<Book> actual = bookRepository.findAllByCategoriesId(1L);
        Assertions.assertEquals(1, actual.size());
    }
}
