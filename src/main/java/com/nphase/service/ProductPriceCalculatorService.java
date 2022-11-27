package com.nphase.service;

import com.nphase.entity.Product;
import java.math.BigDecimal;
import java.util.Optional;

public class ProductPriceCalculatorService {

  public BigDecimal calculateProductPrice(Product product) {
    return Optional.ofNullable(product)
        .map(p -> p.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
        .orElse(BigDecimal.ZERO);
  }
}
