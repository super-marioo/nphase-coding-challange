package com.nphase.entity;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class Product {
  String name;
  BigDecimal pricePerUnit;
  int quantity;
}
