package com.project.springbootproject.mapper;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.model.Book;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper{
    @Override
    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(bookDto.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(bookDto.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(bookDto.getCoverImage());
        return bookDto;
    }
}
