package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShoppingCartServiceTest {
  private final ShoppingCartService cut = new ShoppingCartService();

  @Test
  public void
      given2ValidProductWithDifferentQuantity_calculateTotalPrice_shouldReturnExpectedValue() {
    // given
    var cart =
        new ShoppingCart(
            Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1)));

    // when
    var result = cut.calculateTotalPrice(cart);

    // then
    // 5 * 2 (tea) + 6.5 * 1 (Coffee) = 16.5
    Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
  }
}
