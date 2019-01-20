package com.chintan.shopifychallenge.controllers;

import com.chintan.shopifychallenge.models.Product;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(StrictStubs.class)
public class ProductsControllerTest {
  private final List<Product> productsList = Lists.newArrayList(
      new Product(1, "1", new BigDecimal(1.99), 2),
      new Product(2, "2", new BigDecimal(1.99), 3));

  @Mock(answer = Answers.RETURNS_SMART_NULLS)
  private ProductService mockProductService;

  private ProductsController productsController;

  @Before
  public void setup() {
    productsController = new ProductsController(mockProductService);
  }

  @Test
  public void getAllProducts_happypath() {
    when(mockProductService.getAllProducts()).thenReturn(productsList);

    List<Product> actualProducts = productsController.getAllProducts();

    assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(productsList);
  }

  @Test
  public void getAllProducts_returns_empty_list_when_no_products_present() {
    when(mockProductService.getAllProducts()).thenReturn(Collections.emptyList());

    assertThat(productsController.getAllProducts()).isEmpty();
  }

  @Test
  public void getInStockProducts_happypath() {
    when(productsController.getInStockProducts(true)).thenReturn(productsList);

    List<Product> actualProducts = productsController.getInStockProducts(true);

    verify(mockProductService, times(0)).getAllProducts();
    assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(productsList);
  }

  @Test
  public void getInStockProducts_returns_all_products_if_we_pass_in_false() {
    when(mockProductService.getAllProducts()).thenReturn(productsList);

    List<Product> actualProducts = productsController.getInStockProducts(false);

    verify(mockProductService, times(0)).getAllInStockProducts();
    assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(productsList);
  }

  @Test
  public void getInStockProducts_returns_empty_list_when_no_products_present() {
    when(mockProductService.getAllInStockProducts()).thenReturn(Collections.emptyList());

    verify(mockProductService, times(0)).getAllProducts();
    assertThat(productsController.getInStockProducts(true)).isEmpty();
  }

  @Test
  public void getProductsByTitle_happypath() {
    final String expectedTitle = "Same Title";
    when(mockProductService.getProductsByTitle(expectedTitle)).thenReturn(productsList);

    List<Product> actualProducts = productsController.getProductsByTitle(expectedTitle);

    verify(mockProductService, times(0)).getAllProducts();
    assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(productsList);
  }


  @Test
  public void getProductsByTitle_returns_no_products_if_title_does_not_match() {
    when(mockProductService.getProductsByTitle(anyString())).thenReturn(Collections.emptyList());

    verify(mockProductService, times(0)).getAllProducts();
    assertThat(productsController.getProductsByTitle("non-matching-title")).isEmpty();
  }

  @Test
  public void getProductsByTitle_throws_exception_when_passed_in_no_title() {
    assertThatThrownBy(() -> productsController.getProductsByTitle(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(ProductsController.EMPTY_PRODUCT_TITLE_ERROR_MESSAGE);
  }

  @Test
  public void getSingleProduct_happypath() {
    final Integer expectedProductId = 1;
    final Product singleProduct = new Product(expectedProductId, "1", new BigDecimal(1.99), 2);

    when(mockProductService.getProductById(expectedProductId)).thenReturn(Optional.of(singleProduct));

    assertThat(productsController.getSingleProduct(expectedProductId))
        .isEqualTo(singleProduct);
  }

  @Test
  public void getSingleProduct_throws_exception_when_product_is_nonexistent() {
    when(mockProductService.getProductById(anyInt())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productsController.getSingleProduct(10))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(ProductsController.PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE);
  }
}