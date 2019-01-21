package com.chintan.shopifychallenge.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * This model class is defining how a row in the table "product" would look in the database.
 *
 * All the fields defined will be columns in the table and we create the rows through our endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
  public static final String STOCK_RUN_OUT_EXCEPTION_MESSAGE = "This product cannot be purchased since stock has run out.";

  @Id
  @GeneratedValue
  private Integer productId;

  // NOTE: This is not the ID because there are cases where products could have the same title
  // but not be the same product.
  private String title;
  private BigDecimal price;
  private int inventoryCount;

  public boolean inStock() {
    return this.inventoryCount > 0;
  }

  public void buyProduct() {
    if (this.inventoryCount == 0) throw new IllegalStateException(STOCK_RUN_OUT_EXCEPTION_MESSAGE);
    this.inventoryCount--;
  }
}
