package com.project.springbootproject.mapper;

import com.project.springbootproject.dto.BookDto;
import com.project.springbootproject.model.Book;

public interface BookMapper {
    BookDto toDto(Book book);
}
