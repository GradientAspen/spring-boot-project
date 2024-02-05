package com.project.springbootproject.repository.book;

import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    void deleteById(Long id);

    BookRequestDto findBookByIsbn(String isbn);

    List<Book> findAllByCategoriesId(Long categoryId);

}
