package com.chintan.shopifychallenge.services;

import com.chintan.shopifychallenge.models.Cart;
import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.repository.CartRepository;
import com.chintan.shopifychallenge.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner.StrictStubs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(StrictStubs.class)
public class CartServiceTest {
  private final Cart emptyCart = new Cart();

  private final Product productOne = new Product(1, "1", new BigDecimal(1.00), 5);
  private final Product productTwo = new Product(2, "2", new BigDecimal(2.00), 2);
  private final List<Product> productsList = Lists.newArrayList(productOne, productTwo);

  @Mock(answer = Answers.RETURNS_DEFAULTS)
  private CartRepository mockCartRepository;
  @Mock(answer = Answers.RETURNS_DEFAULTS)
  private ProductRepository mockProductRepository;

  private CartService cartService;

  @Before
  public void setup() {
    cartService = new CartService(mockProductRepository, mockCartRepository);
  }

  @Test
  public void createNewCart_happypath() {
    cartService.createNewCart(productsList);

    final Cart expectedCart = new Cart(null, productsList, new BigDecimal(3.00));
    verify(mockCartRepository).save(expectedCart);
  }

  @Test
  public void createNewCart_without_products() {
    cartService.createNewCart();

    verify(mockCartRepository).save(emptyCart);
  }

  @Test
  public void addNewProductsToCart_happypath() {
    final int expectedCartID = 2;
    when(mockCartRepository.findById(expectedCartID)).thenReturn(Optional.of(emptyCart));

    cartService.addNewProductsToCart(expectedCartID, productOne);

    final Cart expectedCart = new Cart(null, Lists.newArrayList(productOne), new BigDecimal(1.00));
    verify(mockCartRepository).save(expectedCart);
  }

  @Test
  public void addNewProductsToCart_throws_exception_when_cart_is_nonexistent() {
    when(mockCartRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> cartService.addNewProductsToCart(0, productOne))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(CartService.CART_DOES_NOT_EXIST_EXCEPTION_MESSAGE);
  }

  @Test
  public void addNewProductsToCart_throws_exception_when_same_product_is_added() {
    final int expectedCartID = 2;
    final Cart initialCart = new Cart(expectedCartID, productsList, new BigDecimal(3.00));
    when(mockCartRepository.findById(expectedCartID)).thenReturn(Optional.of(initialCart));

    assertThatThrownBy(() -> cartService.addNewProductsToCart(expectedCartID, productOne))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(Cart.PRODUCT_ALREADY_IN_CART_EXCEPTION_MESSAGE);
  }

  @Test
  public void completeCartPurchase_happypath() {
    final int expectedCartID = 2;
    final Cart initialCart = new Cart(null, productsList, new BigDecimal(3.00));

    when(mockCartRepository.findById(expectedCartID)).thenReturn(Optional.of(initialCart));

    cartService.completeCartPurchase(expectedCartID);

    List<Product> expectedProducts = purchaseProducts(productsList);
    final Cart expectedCart = new Cart(null, expectedProducts, new BigDecimal(3.00));

    verify(mockProductRepository).saveAll(expectedProducts);
    verify(mockCartRepository).delete(expectedCart);
  }

  @Test
  public void completeCartPurchase_throws_exception_when_product_is_out_of_stock() {
    final Product outOfStockProduct = new Product(3, "out of stock", new BigDecimal(1.00), 0);
    final Cart outOfStockCart = new Cart(null, Lists.newArrayList(outOfStockProduct), new BigDecimal(1.00));

    when(mockCartRepository.findById(anyInt())).thenReturn(Optional.of(outOfStockCart));

    assertThatThrownBy(() -> cartService.completeCartPurchase(0))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining(Product.STOCK_RUN_OUT_EXCEPTION_MESSAGE);
  }

  @Test
  public void completeCartPurchase_throws_exception_when_cart_is_nonexistent() {
    when(mockCartRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> cartService.completeCartPurchase(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(CartService.CART_DOES_NOT_EXIST_EXCEPTION_MESSAGE);
  }

  private List<Product> purchaseProducts(final List<Product> productsToPurchase) {
    productsToPurchase.forEach(Product::buyProduct);
    return productsToPurchase;
  }
}