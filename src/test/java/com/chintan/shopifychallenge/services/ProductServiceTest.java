package com.chintan.shopifychallenge.services;

import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.repository.ProductRepository;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner.StrictStubs;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(StrictStubs.class)
public class ProductServiceTest {

  private final List<Product> allProducts = Lists.newArrayList(
      new Product(1, "product1", new BigDecimal(3.59), 9),
      new Product(2, "product2", new BigDecimal(50.99), 10),
      new Product(3, "product3", new BigDecimal(1.99), 2));

  @Mock(answer = Answers.RETURNS_SMART_NULLS)
  private ProductRepository mockProductRepository;

  private ProductService productService;

  @Before
  public void setup() {
    productService = new ProductService(mockProductRepository);
  }

  @Test
  public void getAllProducts_happypath() {
    when(mockProductRepository.findAll()).thenReturn(allProducts);

    final List<Product> actualProducts = productService.getAllProducts();

    assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(allProducts);
  }

  @Test
  public void getProductsByTitle_happypath() {
    final String sameTitle = "Same Title";
    final Product sameTitleProductOne = new Product(1, sameTitle, new BigDecimal(3.99), 1);
    final Product sameTitleProductTwo = new Product(2, sameTitle, new BigDecimal(4.99), 2);

    final List<Product> allProducts = Lists.newArrayList(sameTitleProductOne, sameTitleProductTwo,
        new Product(2, "product2", new BigDecimal(50.99), 10));

    when(mockProductRepository.findAll()).thenReturn(allProducts);

    final List<Product> actualProducts = productService.getProductsByTitle(sameTitle);

    assertThat(actualProducts).containsExactlyInAnyOrder(sameTitleProductOne, sameTitleProductTwo);
  }

  @Test
  public void getProductsByTitle_returns_empty_list_when_no_titles_match() {
    when(mockProductRepository.findAll()).thenReturn(allProducts);

    final List<Product> actualProducts = productService.getProductsByTitle("non-existent product title");

    assertThat(actualProducts).isEmpty();
  }

  @Test
  public void getProductById_happypath() {
    final int expectedId = 2;
    final Optional<Product> expectedProduct = Optional.of(
        new Product(2, "2", new BigDecimal(3.99), 2));

    when(mockProductRepository.findById(expectedId)).thenReturn(expectedProduct);

    final Optional<Product> actualProduct = productService.getProductById(expectedId);

    assertThat(actualProduct)
        .isNotEmpty()
        .isEqualTo(expectedProduct);
  }

  @Test
  public void getProductById_returns_empty_optional() {
    final int expectedId = 2;
    when(mockProductRepository.findById(expectedId)).thenReturn(Optional.empty());

    assertThat(productService.getProductById(expectedId)).isEmpty();
  }
}