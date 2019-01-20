package com.chintan.shopifychallenge.controllers;

import com.chintan.shopifychallenge.models.Cart;
import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.models.controllers.CreateCart;
import com.chintan.shopifychallenge.services.CartService;
import com.chintan.shopifychallenge.services.ProductService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner.StrictStubs;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(StrictStubs.class)
public class CartsControllerTest {
  private final Integer cartId = 1;
  private final Cart emptyCart = new Cart(cartId, Collections.emptyList(), BigDecimal.ZERO);

  private final Integer productId = 1;
  private final Product singleProduct = new Product(productId, "1", new BigDecimal(1.00), 2);

  @Mock(answer = Answers.RETURNS_SMART_NULLS)
  private CartService mockCartService;
  @Mock(answer = Answers.RETURNS_SMART_NULLS)
  private ProductService mockProductService;

  private CartsController cartsController;

  @Before
  public void setup() {
    cartsController = new CartsController(mockCartService, mockProductService);
  }

  @Test
  public void viewCart_happypath() {
    when(mockCartService.viewCart(cartId)).thenReturn(Optional.of(emptyCart));

    assertThat(cartsController.viewCart(cartId)).isEqualTo(emptyCart);
  }

  @Test
  public void viewCart_throws_exception_if_cart_is_nonexistent() {
    when(mockCartService.viewCart(anyInt())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> cartsController.viewCart(0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(CartsController.CART_DOES_NOT_EXIST_ERROR_MESSAGE);
  }

  @Test
  public void createCart_happypath() {
    when(mockCartService.createNewCart()).thenReturn(emptyCart);

    assertThat(cartsController.createCart()).isEqualTo(emptyCart);
    verifyNoMoreInteractions(mockCartService, mockProductService);
  }

  @Test
  public void createCart_with_list_of_productIds_happypath() {
    final List<Integer> productIds = Lists.newArrayList(productId);

    when(mockProductService.getProductById(productId)).thenReturn(Optional.of(singleProduct));
    when(mockCartService.createNewCart(Lists.newArrayList(singleProduct))).thenReturn(emptyCart);

    assertThat(cartsController.createCart(new CreateCart(productIds))).isEqualTo(emptyCart);
    verifyNoMoreInteractions(mockCartService, mockProductService);
  }

  @Test
  public void createCart_filters_out_nonexistent_products() {
    // When all product fetch requests return an empty optional, we should pass in an empty list into createNewCart()
    when(mockProductService.getProductById(anyInt())).thenReturn(Optional.empty());
    when(mockCartService.createNewCart(Collections.emptyList())).thenReturn(emptyCart);

    assertThat(cartsController.createCart(new CreateCart(Lists.newArrayList(1, 2, 3))))
        .isEqualTo(emptyCart);
    verifyNoMoreInteractions(mockCartService, mockProductService);
  }

  @Test
  public void addProductToCart_happypath() {
    when(mockProductService.getProductById(productId)).thenReturn(Optional.of(singleProduct));
    when(mockCartService.addNewProductsToCart(cartId, singleProduct)).thenReturn(emptyCart);

    assertThat(cartsController.addProductToCart(cartId, productId)).isEqualTo(emptyCart);
    verifyNoMoreInteractions(mockCartService, mockProductService);
  }

  @Test
  public void addProductToCart_throws_exception_when_product_is_nonexistent() {
    when(mockProductService.getProductById(anyInt())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> cartsController.addProductToCart(cartId, productId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(ProductsController.PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE);
  }

  @Test
  public void addProductToCart_throws_exception_when_cart_is_nonexistent() {
    final RuntimeException expectedException = new RuntimeException();

    when(mockProductService.getProductById(productId)).thenReturn(Optional.of(singleProduct));
    doThrow(expectedException).when(mockCartService).addNewProductsToCart(cartId, singleProduct);

    assertThatThrownBy(() -> cartsController.addProductToCart(cartId, productId))
        .isEqualTo(expectedException);
    verifyNoMoreInteractions(mockCartService, mockProductService);
  }

  @Test
  public void completePurchase_throws_exception_when_cart_is_nonexistent() {
    final RuntimeException expectedException = new RuntimeException();

    doThrow(expectedException).when(mockCartService).completeCartPurchase(cartId);

    assertThatThrownBy(() -> cartsController.completePurchase(cartId))
        .isEqualTo(expectedException);
    verifyNoMoreInteractions(mockCartService, mockProductService);
  }
}