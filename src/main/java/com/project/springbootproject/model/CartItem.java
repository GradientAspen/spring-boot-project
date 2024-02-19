package com.project.springbootproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
