package com.chintan.shopifychallenge.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
  @Id
  @GeneratedValue
  private String productId;

  private String title;
  private BigDecimal price;
  private int inventoryCount;
}
