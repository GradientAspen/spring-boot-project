package com.project.springbootproject.repository.book;

import com.project.springbootproject.dto.BookSearchParameters;
import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.SpecificationBuilder;
import com.project.springbootproject.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String DESCRIPTION = "description";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR)
                    .getSpecification(bookSearchParameters.authors()));
        }
        if (bookSearchParameters.description() != null
                && bookSearchParameters.description().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(DESCRIPTION)
                    .getSpecification(bookSearchParameters.description()));
        }
        return specification;
    }
}
