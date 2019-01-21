package com.chintan.shopifychallenge.services;

import com.chintan.shopifychallenge.models.Cart;
import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.repository.CartRepository;
import com.chintan.shopifychallenge.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
  public static final String CART_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "This cart does not exist.";

  private final ProductRepository productRepository;
  private final CartRepository cartRepository;

  @Autowired
  public CartService(final ProductRepository productRepository, final CartRepository cartRepository) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
  }

  public Optional<Cart> viewCart(final Integer cartId) {
    return cartRepository.findById(cartId);
  }

  public Cart createNewCart() {
    return createNewCart(Collections.emptyList());
  }

  public Cart createNewCart(final List<Product> productsInCart) {
    final Cart newCart = new Cart();
    productsInCart.forEach(newCart::addProduct);

    return cartRepository.save(newCart);
  }

  public Cart addNewProductsToCart(final Integer cartId, final Product productToAdd) {
    final Cart existingCart = getCartIfItExists(cartId);
    existingCart.addProduct(productToAdd);

    return cartRepository.save(existingCart);
  }

  public void completeCartPurchase(final Integer cartId) {
    final Cart existingCart = getCartIfItExists(cartId);

    // Buy all the products from the cart to decrease their inventory and update the database
    existingCart.getProducts().forEach(Product::buyProduct);
    productRepository.saveAll(existingCart.getProducts());

    cartRepository.delete(existingCart);
  }

  private Cart getCartIfItExists(final Integer cartId) {
    final Optional<Cart> cart = cartRepository.findById(cartId);
    if (!cart.isPresent()) throw new IllegalArgumentException(CART_DOES_NOT_EXIST_EXCEPTION_MESSAGE);

    return cart.get();
  }
}
