package com.chintan.shopifychallenge.controllers;

import com.chintan.shopifychallenge.models.Cart;
import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.models.controllers.CreateCart;
import com.chintan.shopifychallenge.services.CartService;
import com.chintan.shopifychallenge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartsController {
  public static final String CART_DOES_NOT_EXIST_ERROR_MESSAGE = "This cart does not exist in the database.";

  private final CartService cartService;
  private final ProductService productService;

  @Autowired
  public CartsController(final CartService cartService, final ProductService productService) {
    this.cartService = cartService;
    this.productService = productService;
  }

  @GetMapping("{cartId}")
  public Cart viewCart(@PathVariable final Integer cartId) {
    Optional<Cart> singleCart = cartService.viewCart(cartId);

    if (!singleCart.isPresent()) throw new IllegalArgumentException(CART_DOES_NOT_EXIST_ERROR_MESSAGE);
    return singleCart.get();
  }

  @PostMapping
  public Cart createCart() {
    return cartService.createNewCart();
  }

  @PostMapping
  public Cart createCart(@RequestBody CreateCart createCart) {
    List<Product> productsInCart = createCart.getProductIds()
        .stream()
        .map(productService::getProductById)
        .filter(Optional::isPresent) // Filter out all products that do not exist
        .map(Optional::get)
        .collect(Collectors.toList());

    return cartService.createNewCart(productsInCart);
  }

  @PostMapping("{cartId}/products/{productId}")
  public Cart addProductToCart(@PathVariable final Integer cartId, @PathVariable final Integer productId) {
    final Optional<Product> productToAdd = productService.getProductById(productId);
    if (!productToAdd.isPresent()) throw new IllegalArgumentException(ProductsController.PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE);

    return cartService.addNewProductsToCart(cartId, productToAdd.get());
  }

  @PutMapping("{cartId}/complete")
  public void completePurchase(@PathVariable final Integer cartId) {
    cartService.completeCartPurchase(cartId);
  }
}
