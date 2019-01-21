package com.chintan.shopifychallenge.controllers;

import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.services.ProductService;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsController {
  public static final String EMPTY_PRODUCT_TITLE_ERROR_MESSAGE = "You cannot pass in an empty product title.";
  public static final String PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE = "This product does not exist in the database.";

  private final ProductService productService;

  @Autowired
  public ProductsController(final ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping(params = "showInStockOnly")
  public List<Product> getInStockProducts(@RequestParam(required = false, defaultValue = "false") final boolean showInStockOnly) {
    if (showInStockOnly) return productService.getAllInStockProducts();
    else return getAllProducts();
  }

  @GetMapping(params = "productTitle")
  public List<Product> getProductsByTitle(@RequestParam(required = false) final String productTitle) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(productTitle), EMPTY_PRODUCT_TITLE_ERROR_MESSAGE);
    return productService.getProductsByTitle(productTitle);
  }

  @GetMapping("{productId}")
  public Product getSingleProduct(@PathVariable final Integer productId) {
    Optional<Product> singleProduct = productService.getProductById(productId);

    if (!singleProduct.isPresent()) throw new IllegalArgumentException(PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE);
    return singleProduct.get();
  }
}
