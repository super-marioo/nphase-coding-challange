package com.nphase.service;

import com.nphase.configuration.DiscountConfiguration;
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

  private final DiscountConfiguration discountConfiguration;

  private final ProductPriceCalculatorService productPriceCalculatorService;

  public BigDecimal getCartDiscountAmount(ShoppingCart shoppingCart) {
    Optional.ofNullable(shoppingCart)
        .orElseThrow(() -> new RuntimeException("ShoppingCart cannot be null!"));

    var categoryToProducts =
        shoppingCart.getProducts().stream().collect(Collectors.groupingBy(Product::getCategory));

    return categoryToProducts.values().stream()
        .filter(
            products ->
                countQuality(products) >= discountConfiguration.getMinItemsQuantityInCategory())
        .map(
            products ->
                sumCartAmount(products)
                    .multiply(
                        new QualityDiscount(discountConfiguration.getDiscountPercent())
                            .priceMultiplier()))
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

  @RequiredArgsConstructor
  static class QualityDiscount implements Discount {
    private final BigDecimal percent;

    public BigDecimal percent() {
      return percent;
    }
  }
}
