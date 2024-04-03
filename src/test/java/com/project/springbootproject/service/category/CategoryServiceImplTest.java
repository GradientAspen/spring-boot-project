package com.project.springbootproject.service.category;

import com.project.springbootproject.dto.categorydto.CategoryDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.CategoryMapper;
import com.project.springbootproject.model.Category;
import com.project.springbootproject.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find all categories")
    void findAll_ReturnsListOfCategoryDto() {
        // Given
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        categories.add(category1);

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("Category 1");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Category 2");

        List<CategoryDto> expectedCategoryDtos = new ArrayList<>();
        expectedCategoryDtos.add(categoryDto1);
        expectedCategoryDtos.add(categoryDto2);

        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        // When
        List<CategoryDto> foundCategoryDtos = categoryService.findAll();

        // Then
        assertNotNull(foundCategoryDtos);
        assertEquals(2, foundCategoryDtos.size());
        assertEquals(expectedCategoryDtos, foundCategoryDtos);
    }

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get category by Id")
    void getById_ExistingId_ReturnsCategoryDto() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Category Name");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(categoryId);
        expectedCategoryDto.setName("Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedCategoryDto);

        // When
        CategoryDto foundCategoryDto = categoryService.getById(categoryId);

        // Then
        assertNotNull(foundCategoryDto);
        assertEquals(expectedCategoryDto, foundCategoryDto);
    }

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get category by id throw exception")
    void getById_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(categoryId));
    }

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("SAve category in DB")
    void save_ValidCategoryDto_ReturnsSavedCategoryDto() {
        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Test Category");

        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(1L);
        expectedCategoryDto.setName("Test Category");

        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedCategoryDto);

        // When
        CategoryDto savedCategoryDto = categoryService.save(categoryDto);

        // Then
        assertNotNull(savedCategoryDto);
        assertEquals(expectedCategoryDto, savedCategoryDto);
    }

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update category in DB OK")
    void update_ExistingIdAndValidCategoryDto_SuccessfulUpdate() {
        // Given
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Original Category");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Category");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        doNothing().when(categoryMapper).updateCategoryFromCategoryDto(categoryDto, existingCategory);

        // When
        categoryService.update(categoryId, categoryDto);

        // Then
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).updateCategoryFromCategoryDto(categoryDto, existingCategory);
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    @Sql(scripts = "classpath:database/category/delete-all-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update category in DB throw Exception")
    void update_NonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(categoryId, categoryDto));
    }

    @Test
    @DisplayName("Delete Category bu Id")
    public void deleteById_ValidId_SuccessfulDeletion() {
        // Given
        Long categoryId = 1L;

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository).deleteById(categoryId);
    }
}