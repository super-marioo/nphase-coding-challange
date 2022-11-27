package com.nphase.configuration;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.Getter;

@Getter
public class DiscountConfiguration {
  public final int minItemsQuantityInCategory;
  public final BigDecimal discountPercent;

  public DiscountConfiguration(int minItemsQuantityInCategory, BigDecimal discountPercent) {
    if (minItemsQuantityInCategory < 0) {
      throw new WrongDiscountConfigurationException(
          "Wrong configuration - minItemsQuantityInCategory must be positive-or-zero");
    }

    Optional.ofNullable(discountPercent)
        .orElseThrow(
            () ->
                new WrongDiscountConfigurationException(
                    "Wrong configuration - DiscountPercent cannot be null!"));

    if(discountPercent.signum() < 0) {
      throw new WrongDiscountConfigurationException(
          "Wrong configuration - discountPercent positive-or-zero");
    }

    if(discountPercent.compareTo(new BigDecimal("100")) > 0) {
      throw new WrongDiscountConfigurationException(
          "Wrong configuration - discountPercent cannot be above 100%");
    }

    this.minItemsQuantityInCategory = minItemsQuantityInCategory;
    this.discountPercent = discountPercent;
  }
}
