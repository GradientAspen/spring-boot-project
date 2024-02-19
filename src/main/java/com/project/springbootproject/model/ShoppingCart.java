package com.project.springbootproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data

@Entity

@Table(name = "shopping_carts")

@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = TRUE WHERE id = ?")

@SQLRestriction("is_deleted = FALSE")

public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany(mappedBy = "shoppingCart", orphanRemoval = true)
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    private Set<CartItem> cartItems;

    @Column(nullable = false)
    private boolean isDeleted = false;

}
