package com.chintan.shopifychallenge.controllers;

import com.chintan.shopifychallenge.models.Cart;
import com.chintan.shopifychallenge.models.Product;
import com.chintan.shopifychallenge.models.controllers.CreateCart;
import com.chintan.shopifychallenge.services.CartService;
import com.chintan.shopifychallenge.services.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/carts", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "Endpoints for creating new carts, adding products to a cart and completing purchases.")
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
  @ApiOperation("Returns the cart details that includes a list of products and totalCost for the given cart. " +
                "An exception is thrown if the cart is not found in the database.")
  @ApiImplicitParam(name = "cartId", value = "The ID of the cart that you want details of", dataType = "int",
                    required = true, paramType = "path")
  public Cart viewCart(@PathVariable final Integer cartId) {
    Optional<Cart> singleCart = cartService.viewCart(cartId);

    if (!singleCart.isPresent()) throw new IllegalArgumentException(CART_DOES_NOT_EXIST_ERROR_MESSAGE);
    return singleCart.get();
  }

  @PostMapping
  @ApiOperation("Returns a newly created cart. If the list of productIDs were passed in, the cart returned will reflect that.")
  public Cart createCart(@ApiParam(value = "OPTIONAL: You may create a new cart with a list of products. " +
                                           "The list should contain product IDs that you want in the cart.")
                         @RequestBody(required = false) CreateCart createCart) {
    if (Objects.isNull(createCart) || Objects.isNull(createCart.getProductIds()))
      return cartService.createNewCart();

    List<Product> productsInCart = createCart.getProductIds()
        .stream()
        .map(productService::getProductById)
        .filter(Optional::isPresent) // Filter out all products that do not exist
        .map(Optional::get)
        .collect(Collectors.toList());

    return cartService.createNewCart(productsInCart);
  }

  @PutMapping("{cartId}/products/{productId}")
  @ApiOperation("Returns the updated cart details where the product you specified to be added is present and the " +
                "total cost reflects those changes. An exception is thrown if the product is already present in the cart.")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "cartId", value = "The ID of the cart that you want to add a product to",
                        dataType = "int", required = true, paramType = "path"),
      @ApiImplicitParam(name = "productId", value = "The ID of the product you want to add to the cart",
                        dataType = "int", required = true, paramType = "path")
  })
  public Cart addProductToCart(@PathVariable final Integer cartId, @PathVariable final Integer productId) {
    final Optional<Product> productToAdd = productService.getProductById(productId);
    if (!productToAdd.isPresent()) throw new IllegalArgumentException(ProductsController.PRODUCT_DOES_NOT_EXIST_ERROR_MESSAGE);

    return cartService.addNewProductsToCart(cartId, productToAdd.get());
  }

  @PutMapping("{cartId}/complete")
  @ApiOperation("This will complete the purchase of the products that are in the cart. The product inventory will " +
                "be updated and the cart will get subsequently deleted. An exception is thrown if the cart is not " +
                "present in the database or one of the products is out of stock.")
  @ApiImplicitParam(name = "cartId", value = "The ID of the cart that you complete the purchase for",
                    dataType = "int", required = true, paramType = "path")
  public void completePurchase(@PathVariable final Integer cartId) {
    cartService.completeCartPurchase(cartId);
  }
}
