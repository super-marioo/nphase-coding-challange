package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiscountService {
  private static final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal("100");
  public static final long MIN_QUALITY_AMOUNT_DISCOUNT = 4L;

  private final ProductPriceCalculatorService productPriceCalculatorService;

  public BigDecimal getCartDiscountAmount(ShoppingCart shoppingCart) {
    Optional.ofNullable(shoppingCart)
        .orElseThrow(() -> new RuntimeException("ShoppingCart cannot be null!"));

    var categoryToProducts =
        shoppingCart.getProducts().stream().collect(Collectors.groupingBy(Product::getCategory));

    return categoryToProducts.values().stream()
        .filter(products -> countQuality(products) >= MIN_QUALITY_AMOUNT_DISCOUNT)
        .map(products -> sumCartAmount(products).multiply(new QualityDiscount().priceMultiplier()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal sumCartAmount(List<Product> products) {
    return products.stream()
        .map(productPriceCalculatorService::calculateProductPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private long countQuality(List<Product> products) {
    return products.stream().mapToLong(Product::getQuantity).sum();
  }

  interface Discount {
    BigDecimal percent();

    default BigDecimal priceMultiplier() {
      return percent().divide(ONE_HUNDRED_PERCENT, 8, RoundingMode.HALF_UP);
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
