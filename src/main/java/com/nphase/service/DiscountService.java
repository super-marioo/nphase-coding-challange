package com.nphase.service;

import com.nphase.entity.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class DiscountService {
  private static final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal("100");

  public Discount getDiscount(Product product) {
    return Optional.ofNullable(product)
        .map(Product::getQuantity)
        .filter(quality -> quality > 3)
        .<Discount>map(q -> new QualityDiscount())
        .orElseGet(NoDiscount::new);
  }

  interface Discount {
    BigDecimal percent();

    default BigDecimal priceMultiplier() {
      return DiscountService.ONE_HUNDRED_PERCENT
          .subtract(percent())
          .divide(ONE_HUNDRED_PERCENT, 2, RoundingMode.HALF_UP);
    }
  }

  static class NoDiscount implements Discount {
    public BigDecimal percent() {
      return BigDecimal.ZERO;
    }
  }

  static class QualityDiscount implements Discount {
    public BigDecimal percent() {
      return BigDecimal.TEN;
    }
  }
}
