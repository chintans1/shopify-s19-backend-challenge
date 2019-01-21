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
  public static final String PRODUCT_ALREADY_IN_CART_EXCEPTION_MESSAGE = "This product is already present in the cart";

  @Id
  @GeneratedValue
  private Integer cartId;

  @Setter(AccessLevel.NONE)
  @ManyToMany
  private List<Product> products = new ArrayList<>();

  @Setter(AccessLevel.NONE)
  private BigDecimal totalCost = BigDecimal.ZERO;

  public void addProduct(final Product product) {
    if (products.contains(product)) throw new IllegalArgumentException(PRODUCT_ALREADY_IN_CART_EXCEPTION_MESSAGE);
    this.products.add(product);
    this.totalCost = this.totalCost.add(product.getPrice());
  }

  // FIXME: Future addition would be to allow removal of products that are present in the cart already
  public void removeProduct(final Product product) {
    if (!this.products.contains(product))
      throw new IllegalArgumentException(PRODUCT_NOT_PRESENT_EXCEPTION_MESSAGE);

    this.products.remove(product);
    this.totalCost = this.totalCost.subtract(product.getPrice());
  }
}
