package com.project.springbootproject.service.category;

import com.project.springbootproject.dto.categoryDto.CategoryDto;
import com.project.springbootproject.model.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
