package com.chintan.shopifychallenge.controllers;

import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.services.ProductService;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Endpoints for fetching all products, fetching in-stock products or fetching specific products using title or ID.")
public class ProductsController {
  public static final String EMPTY_PRODUCT_TITLE_ERROR_MESSAGE = "You cannot pass in an empty product title.";
  public static final String PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE = "This product does not exist in the database.";

  private final ProductService productService;

  @Autowired
  public ProductsController(final ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @ApiOperation("Returns all the products that are currently present in the marketplace.")
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @GetMapping(params = "showInStockOnly")
  @ApiOperation("Returns all in-stock products that are currently present in the marketplace if showInStockOnly is true. " +
                "Otherwise, all the products are returned.")
  @ApiImplicitParam(name = "showInStockOnly", value = "Boolean to denote whether you want to fetch only in-stock products or not.",
                    dataType = "boolean", required = true, paramType = "query")
  public List<Product> getInStockProducts(@RequestParam(required = false, defaultValue = "false") final boolean showInStockOnly) {
    if (showInStockOnly) return productService.getAllInStockProducts();
    else return getAllProducts();
  }

  @GetMapping(params = "productTitle")
  @ApiOperation("Returns all products present in the marketplace that match the product title you specify.")
  @ApiImplicitParam(name = "productTitle", value = "The title of product that you want to see details for.",
                    dataType = "string", required = true, paramType = "query")
  public List<Product> getProductsByTitle(@RequestParam(required = false) final String productTitle) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(productTitle), EMPTY_PRODUCT_TITLE_ERROR_MESSAGE);
    return productService.getProductsByTitle(productTitle);
  }

  @GetMapping("{productId}")
  @ApiOperation("Return details for a single product using the given product ID. " +
                "An exception is thrown if the product is not present in the marketplace.")
  @ApiImplicitParam(name = "productId", value = "The ID of the product that you wants details of", dataType = "int",
                    required = true, paramType = "path")
  public Product getSingleProduct(@PathVariable final Integer productId) {
    Optional<Product> singleProduct = productService.getProductById(productId);

    if (!singleProduct.isPresent()) throw new IllegalArgumentException(PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE);
    return singleProduct.get();
  }
}
