package com.infinity.product.controller;

import com.infinity.product.domain.Product;
import com.infinity.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@Slf4j
public class ProductController {

    public final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Returns The product with specified ID.
     *
     * @param id    The ID of product to retrieve.
     * @return      The product with the specified ID.
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        return productService.findById(id)
                .map(product -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(product.getId()))
                                .location(new URI("/product/" + product.getId()))
                                .body(product);

                    } catch (URISyntaxException e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Returns all products in the database.
     *
     * @return Returns all products in the database.
     */

    @GetMapping("/products")
    public Iterable<Product> getProducts() {
        return productService.findAll();
    }

    /**
     * Creates a new product.
     *
     * @param product The Products to create.
     * @return The Created product.
     */
    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {

        log.info("Creating new product with name: {}, quantity: {}", product.getName(), product.getQuantity());
        // Create the new product
        Product newProduct = productService.save(product);

        // Build a created response
        try {
            return ResponseEntity
                    .created(new URI("/product/" + product.getId()))
                    .eTag(Integer.toString(newProduct.getVersion()))
                    .body(newProduct);
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product,
                                           @PathVariable Integer id,
                                           @RequestHeader("If-Match") Integer ifMatch) {
        log.info("Updating product with id: {}, name: {}, quantity: {}", id, product.getName(), product.getQuantity());

        // Get The existing product.
        Optional<Product> existingProduct = productService.findById(id);

        return existingProduct.map(p -> {
            // Compare the eTag version
            log.info("Product with ID: {} has a version of {}. Update is for If-Match: {}"
                    , id, p.getVersion(), ifMatch);
            if (!p.getVersion().equals(ifMatch))
                return ResponseEntity.status(HttpStatus.CONFLICT).build();

            // Update Product
            p.setName(product.getName());
            p.setQuantity(product.getQuantity());
            p.setVersion(p.getVersion() + 1);

            log.info("Updating product with id: {} -> name= {}, quantity= {}, version={}"
                    , p.getId(), p.getName(), p.getQuantity(), p.getVersion());

            try {
                // Update the product and return an ok response
                if (productService.update(p)) {
                    return ResponseEntity
                            .ok()
                            .location(new URI("/product/" + p.getId()))
                            .eTag(Integer.toString(p.getVersion()))
                            .body(p);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (URISyntaxException e) {
                return ResponseEntity.internalServerError().build();
            }
        }).orElse(ResponseEntity.notFound().build());

    }

    /**
     * Deletes the product with the specified ID.
     *
     * @param id The ID of the Product to delete.
     * @return A ResponseEntity with one of the following status:
     * 200 OK if the deleted was successfully
     * 404 Not Found if the product with given id are not exists
     * 500 Internal Server Error if an error occur during deleted deletion
     */
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {

        // Get the existing product
        Optional<Product> existingProduct = productService.findById(id);
        log.info("Deleting Product with ID: {}", id);
        return existingProduct.map(p -> {
            if (productService.delete(p.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
