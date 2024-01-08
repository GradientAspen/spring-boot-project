package com.project.springbootproject.validation;

import com.project.springbootproject.dto.BookRequestDto;
import com.project.springbootproject.repository.book.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<UniqueIsbn, String> {
    private final BookRepository bookRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        return bookRepository.findBookByIsbn(value) == null;
    }
}
