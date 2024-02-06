package com.project.springbootproject.controller;

import com.project.springbootproject.dto.BookDtoWithoutCategoryIds;
import com.project.springbootproject.dto.categoryDto.CategoryDto;
import com.project.springbootproject.model.Category;
import com.project.springbootproject.service.BookService;
import com.project.springbootproject.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
@Transactional
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create category in DB",
            description = "Add Category in Db. " +
                    "Only user with role Admin have ability add category in DB"
    )
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category by Id",
            description = "Get Category from DB by ID")
    public CategoryDto getCategoryById(Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update information about Category",
            description = "Update information about Category. "
                    + "Only users with Role Admin have the ability update "
                    + "information about Category in DB.")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete information about category",
            description = "Delete Category. "
                    + "Only users with Role Admin have the ability delete "
                    + "category from DB.")
    public void deleteCategory(Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by Category",
            description = "Get books from DB by Category")
    public List<BookDtoWithoutCategoryIds> getBooksByCategory(Long id) {
        return bookService.findBooksByCategoryId(id);
    }
}
