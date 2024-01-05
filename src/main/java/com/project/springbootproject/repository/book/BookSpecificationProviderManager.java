package com.project.springbootproject.repository.book;

import com.project.springbootproject.model.Book;
import com.project.springbootproject.repository.SpecificationProvider;
import com.project.springbootproject.repository.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider getSpecificationProvider(String key) {
        return bookSpecificationProviders
                .stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new NoSuchElementException("Can not find specification provider for key:"
                        + key));
    }
}
