package com.infinity.product.repository;

import com.infinity.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    /**
     * Returns The product with the specified ID
     *
     * @param id  ID of the product to retrieved.
     * @return  The requested product if found.
     * */
    Optional<Product> findById(Integer id);

    /**
     * Return all products in the database.
     *
     * @return All products in the database.
     * */
    List<Product> findAll();

    /**
     * Update the specified product, identified by its id.
     *
     * @param product The product to update.
     *
     * @return True if the update succeeded, otherwise false
     * */
    Boolean update(Product product);
    /**
     * saves the specified product to database.
     *
     * @param product The product to save to the database.
     *
     * @return The saved product.
     * */
    Product save(Product product);
    /**
     * Delete product with specified id.
     *
     * @param id The id of the product to delete.
     *
     * @return True if operation was successful, otherwise false
     * */
    Boolean delete(Integer id);

}
