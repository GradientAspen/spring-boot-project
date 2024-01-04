package com.project.springbootproject.repository.book;

import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DescriptionSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "description";
    }

    public Specification<Book>getSpecification(String[]params){
        return (root, query, criteriaBuilder) -> root.get("description")
                .in(Arrays.stream(params).toArray());
    }
}
