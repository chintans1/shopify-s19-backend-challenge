package com.chintan.shopifychallenge.models.controllers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor // NOTE: This constructor is needed for the ObjectMapper to deserialize the request body
@AllArgsConstructor
@ApiModel(description = "Object for creating a new cart where you may specify products you want " +
                        "included in the cart from the start.")
public class CreateCart {
  @ApiModelProperty(value = "List of product IDs to denote the products you want in the cart initially.")
  private List<Integer> productIds;
}
