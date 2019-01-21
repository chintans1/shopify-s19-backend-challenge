package com.chintan.shopifychallenge.repository;

import com.chintan.shopifychallenge.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * An instance of this will contain methods to query the table "cart" by using methods such as
 * findAll() and findById() and it also allows you add and "complete" (delete) carts through methods
 * like save() and delete().
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {}
