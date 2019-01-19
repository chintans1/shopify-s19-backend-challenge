package com.chintan.shopifychallenge.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
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

  private List<Product> products;
  private BigDecimal totalCost;

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
