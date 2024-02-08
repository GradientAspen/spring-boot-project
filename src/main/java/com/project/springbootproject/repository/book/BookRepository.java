package com.project.springbootproject.repository.book;

import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    void deleteById(Long id);

    BookRequestDto findBookByIsbn(String isbn);

    @Query("FROM Book b JOIN  b.categories c  WHERE c.id=:categoryId")
    List<Book> findAllByCategoriesId(Long categoryId);

}
