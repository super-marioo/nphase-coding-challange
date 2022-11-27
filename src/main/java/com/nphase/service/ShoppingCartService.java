package com.nphase.service;

import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.util.Optional;

public class ShoppingCartService {

  public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
    Optional.ofNullable(shoppingCart)
        .orElseThrow(() -> new RuntimeException("ShoppingCart cannot be null!"));

    return shoppingCart.getProducts().stream()
        .map(
            product ->
                product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
        .reduce(BigDecimal::add)
        .orElse(BigDecimal.ZERO);
  }
}
