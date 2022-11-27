package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShoppingCartService {

  private final DiscountService discountService;

  public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
    Optional.ofNullable(shoppingCart)
        .orElseThrow(() -> new RuntimeException("ShoppingCart cannot be null!"));

    return shoppingCart.getProducts().stream()
        .map(this::calculateProductPrice)
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }

  private BigDecimal calculateProductPrice(Product product) {
    return product
        .getPricePerUnit()
        .multiply(BigDecimal.valueOf(product.getQuantity()))
        .multiply(discountService.getDiscount(product).priceMultiplier())
        .setScale(1, RoundingMode.HALF_UP);
  }
}
