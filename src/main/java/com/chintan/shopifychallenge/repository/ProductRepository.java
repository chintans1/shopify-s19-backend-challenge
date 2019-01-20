package com.chintan.shopifychallenge.repository;

import com.chintan.shopifychallenge.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * An instance of this will contain methods to query the table "product" by using methods such as
 * findAll() and findById() and it also allows you add and delete products through methods like save() and delete().
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {}