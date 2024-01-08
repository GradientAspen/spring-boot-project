package com.project.springbootproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookRequestDto {
    private Long id;
    @NotNull(message = "Title cannot be null")
    private String title;
    @NotNull(message = "Author cannot be null")
    private String author;
    @NotNull(message = "Isbn cannot be null")
    private String isbn;
    @NotNull(message = "Price cannot be null")
    @Min(message = "Price must be greater than 0", value = 0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
