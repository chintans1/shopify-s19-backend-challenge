package com.chintan.shopifychallenge.models.controllers;

import lombok.Data;

import java.util.List;

@Data
public class CreateCart {
  private final List<Integer> productIds;
}
