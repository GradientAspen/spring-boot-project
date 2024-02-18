package com.project.springbootproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@Data
@Table(name = "cartitems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shoppingcart_id")
    @EqualsAndHashCode.Exclude
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "book_id")
    @NonNull
    @EqualsAndHashCode.Exclude
    private Book book;
    @NonNull
    private int quantity;

    public CartItem() {

    }
}
