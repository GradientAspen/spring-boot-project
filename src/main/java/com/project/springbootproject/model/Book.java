package com.project.springbootproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@SQLDelete(sql = "UPDATE books SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String author;
    @NonNull
    @Column(unique = true)
    private String isbn;
    @NonNull
    private BigDecimal price;
    private String description;
    private String coverImage;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public Book() {

    }
}
