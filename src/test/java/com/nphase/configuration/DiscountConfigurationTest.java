package com.nphase.configuration;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DiscountConfigurationTest {

  @Test
  public void givenProperConfigurationValue_new_shouldCreateConfigurationObject() {
    // given
    var discountPercent = new BigDecimal("7");
    var minItemQuantity = 55;

    // when
    var config = new DiscountConfiguration(minItemQuantity, discountPercent);

    // then
    Assertions.assertEquals(minItemQuantity, config.getMinItemsQuantityInCategory());
    Assertions.assertEquals(discountPercent, config.getDiscountPercent());
  }

  @Test
  public void givenDiscountPercentNull_new_shouldThrowWrongDiscountConfigurationException() {
    // given
    BigDecimal discountPercent = null;
    var minItemQuantity = 55;

    // when
    var wrongDiscountConfigurationException =
        Assertions.assertThrows(
            WrongDiscountConfigurationException.class,
            () -> new DiscountConfiguration(minItemQuantity, discountPercent));

    // then
    Assertions.assertEquals(
        "Wrong configuration - DiscountPercent cannot be null!",
        wrongDiscountConfigurationException.getMessage());
  }

  @Test
  public void givenDiscountPercent101Percent_new_shouldThrowWrongDiscountConfigurationException() {
    // given
    BigDecimal discountPercent = new BigDecimal("100.01");
    var minItemQuantity = 55;

    // when
    var wrongDiscountConfigurationException =
        Assertions.assertThrows(
            WrongDiscountConfigurationException.class,
            () -> new DiscountConfiguration(minItemQuantity, discountPercent));

    // then
    Assertions.assertEquals(
        "Wrong configuration - discountPercent cannot be above 100%",
        wrongDiscountConfigurationException.getMessage());
  }

  @Test
  public void givenDiscountPercentNegative_new_shouldThrowWrongDiscountConfigurationException() {
    // given
    BigDecimal discountPercent = new BigDecimal("-1");
    var minItemQuantity = 55;

    // when
    var wrongDiscountConfigurationException =
        Assertions.assertThrows(
            WrongDiscountConfigurationException.class,
            () -> new DiscountConfiguration(minItemQuantity, discountPercent));

    // then
    Assertions.assertEquals(
        "Wrong configuration - discountPercent positive-or-zero",
        wrongDiscountConfigurationException.getMessage());
  }

  @Test
  public void givenMinItemQuantityNegative_new_shouldThrowWrongDiscountConfigurationException() {
    // given
    BigDecimal discountPercent = new BigDecimal("45");
    var minItemQuantity = -55;

    // when
    var wrongDiscountConfigurationException =
        Assertions.assertThrows(
            WrongDiscountConfigurationException.class,
            () -> new DiscountConfiguration(minItemQuantity, discountPercent));

    // then
    Assertions.assertEquals(
        "Wrong configuration - minItemsQuantityInCategory must be positive-or-zero",
        wrongDiscountConfigurationException.getMessage());
  }
}
