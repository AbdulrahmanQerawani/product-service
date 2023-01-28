package com.infinity.product.service;

import com.infinity.product.domain.Product;

import java.util.List;
import java.util.Optional;
/**
 * @apiNote This is abstract interface Crud-Operations for product service application api.
 *
 * */
public interface ProductService {
    /**
     * Returns the product with specified ID.
     *
     * @param id The ID of the product to be retrieved.
     * @return The requested product if exists.
     */
    Optional<Product> findById(Integer id);

    /**
     * Returns all products in the database.
     *
     * @return All products in the database.
     */
    List<Product> findAll();

    /**
     * Updates the specified product, identified with its ID.
     *
     * @param product The product to update
     * @return True if the update succeeded, otherwise false.
     */
    Boolean update(Product product);

    /**
     * Saves the specified product to the database.
     *
     * @param product The product to save to the database.
     * @return The saved product.
     */
    Product save(Product product);

    /**
     * Deletes the specified product, identified with its ID.
     *
     * @param id The id of the product to delete
     * @return True if the Operation was successful, otherwise false.
     */
    Boolean delete(Integer id);
}
