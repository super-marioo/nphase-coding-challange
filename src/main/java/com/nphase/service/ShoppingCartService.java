package com.nphase.service;

import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShoppingCartService {

  private final DiscountService discountService;
  private final ProductPriceCalculatorService productPriceCalculatorService;

  public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
    Optional.ofNullable(shoppingCart)
        .orElseThrow(() -> new RuntimeException("ShoppingCart cannot be null!"));

    return shoppingCart.getProducts().stream()
        .map(productPriceCalculatorService::calculateProductPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .subtract(discountService.getCartDiscountAmount(shoppingCart))
        .setScale(2, RoundingMode.HALF_UP);
  }
}
