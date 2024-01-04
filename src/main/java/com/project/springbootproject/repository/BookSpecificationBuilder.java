package com.project.springbootproject.repository;

import com.project.springbootproject.dto.BookSearchParameters;
import com.project.springbootproject.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParameters.authors()));
        }
        if (bookSearchParameters.description() != null && bookSearchParameters.description().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("description")
                    .getSpecification(bookSearchParameters.description()));
        }
        return specification;
    }
}
