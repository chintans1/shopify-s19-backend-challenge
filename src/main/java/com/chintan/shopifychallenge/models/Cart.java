package com.chintan.shopifychallenge.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This model class is defining how a row in the table "cart" would look in the database.
 *
 * All the fields defined will be columns in the table and we create the rows through our endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
  public static final String PRODUCT_NOT_PRESENT_EXCEPTION_MESSAGE =
      "This product is not currently present in the cart";

  @Id
  @GeneratedValue
  private Integer cartId;

  @Setter(AccessLevel.NONE)
  @ElementCollection(targetClass=Product.class)
  private List<Product> products = new ArrayList<>();

  @Setter(AccessLevel.NONE)
  private BigDecimal totalCost = BigDecimal.ZERO;

  // NOTE: We allow the same product to be in the products list more than once because the customer is allowed to
  // buy the product with more than 1 quantity.
  public void addProduct(final Product product) {
    this.products.add(product);
    this.totalCost = this.totalCost.add(product.getPrice());
  }

  public void removeProduct(final Product product) {
    if (!this.products.contains(product))
      throw new IllegalArgumentException(PRODUCT_NOT_PRESENT_EXCEPTION_MESSAGE);

    this.products.remove(product);
    this.totalCost = this.totalCost.subtract(product.getPrice());
  }
}
