package com.project.springbootproject.service.category;

import com.project.springbootproject.dto.categorydto.CategoryDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.CategoryMapper;
import com.project.springbootproject.model.Category;
import com.project.springbootproject.repository.category.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can not find Category with id : " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void update(Long id, CategoryDto categoryDto) {
        Optional<Category> category = categoryRepository.findById(id);
        Category categoryUpdate = category.orElseThrow(
                () -> new EntityNotFoundException("Can not find Category with ID: " + id));
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, categoryUpdate);
        categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
