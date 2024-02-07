package com.project.springbootproject.service.category;

import com.project.springbootproject.dto.categorydto.CategoryDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    void update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
