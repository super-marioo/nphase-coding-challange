package com.nphase.entity;

import java.util.List;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class ShoppingCart {
  List<Product> products;
}
