package com.chintan.shopifychallenge.services;

import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  @Autowired
  public ProductService(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public List<Product> getProductsByTitle(final String productTitle) {
    return productRepository.findAll()
        .stream()
        .filter(product -> product.getTitle().equals(productTitle))
        .collect(Collectors.toList());
  }

  public Optional<Product> getProductById(final Integer productId) {
    return productRepository.findById(productId);
  }
}
