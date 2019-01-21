package com.chintan.shopifychallenge.models.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor // NOTE: This constructor is needed for the ObjectMapper to deserialize the request body
@AllArgsConstructor
public class CreateCart {
  private List<Integer> productIds;
}
