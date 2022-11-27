package com.nphase.service;

import com.nphase.configuration.DiscountConfiguration;
import com.nphase.entity.Category;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ShoppingCartServiceTest {
  private final ProductPriceCalculatorService productPriceCalculatorService =
      new ProductPriceCalculatorService();

  private final DiscountConfiguration discountConfiguration =
      new DiscountConfiguration(4, BigDecimal.TEN);
  private final ShoppingCartService cut =
      new ShoppingCartService(
          new DiscountService(discountConfiguration, productPriceCalculatorService),
          productPriceCalculatorService);

  @Test
  public void
      given2ValidProductWithDifferentQuantityAndNoDiscounts_calculateTotalPrice_shouldReturnExpectedValue() {
    // given
    var cart =
        new ShoppingCart(
            Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1, Category.DRINKS)));

    // when
    var result = cut.calculateTotalPrice(cart);

    // then
    // 5 * 2 (tea) + 6.5 * 1 (Coffee) = 16.5
    Assertions.assertEquals(new BigDecimal("16.50"), result);
  }

  @Test
  @Disabled
  // Correct For task #2 logic -> for task 3 logic not
  public void givenProductWith4Items_calculateTotalPrice_shouldUseQualityDiscount() {
    // given
    var cart =
        new ShoppingCart(
            Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3, Category.DRINKS)));

    // when
    var result = cut.calculateTotalPrice(cart);

    // then
    // 5 * 5 (tea) (-10% discount) + 3.5 * 3 (Coffee) (No discount) = 25 * 0.9 + 10.5 = 33
    Assertions.assertEquals(new BigDecimal("33.0"), result);
  }

  @Test
  // Correct task #3 version of test
  public void
      givenProductWith4ItemsAndCategoryDiscount_calculateTotalPrice_shouldUseQualityDiscount() {
    // given
    var cart =
        new ShoppingCart(
            Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3, Category.DRINKS)));

    // when
    var result = cut.calculateTotalPrice(cart);

    // then
    // (5 * 5 (tea) + 3.5 * 3 (Coffee)) (-10% discount)  = (25 + 10.5) * 0.9 = 31.95
    Assertions.assertEquals(new BigDecimal("31.95"), result);
  }

  @Test
  public void
      givenCartWith4ItemsInDrinkCategory_calculateTotalPrice_shouldApplyDiscountToFinalAmount() {
    // given
    var cart =
        new ShoppingCart(
            Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 2, Category.DRINKS),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2, Category.DRINKS),
                new Product("Cheese", BigDecimal.valueOf(8), 2, Category.FOOD)));

    // when
    var result = cut.calculateTotalPrice(cart);

    // then
    // (2 * 5.3 (tea) + 2 * 3.5 (Coffee)) (-10% discount) + 2 * 8 (Cheese) (No discount) = (10.6 + 7
    // ) * 90% + 16 = 15.84 + 16 = 31.84
    Assertions.assertEquals(new BigDecimal("31.84"), result);
  }
}
