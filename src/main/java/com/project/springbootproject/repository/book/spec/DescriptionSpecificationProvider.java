package com.project.springbootproject.repository.book.spec;

import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DescriptionSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "description";
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get("description")
                .in(Arrays.stream(params).toArray());
    }
}
