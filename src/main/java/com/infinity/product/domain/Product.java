package com.infinity.product.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * This is a POJO class that represent Product Table in database
 * id: is primary key for Product.
 * name: is a descriptive name for Product.
 * quantity: an inventory quantity for Product.
 * version: which will use to validate the Product operations are valid.
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
@ToString
@EqualsAndHashCode
public class Product {

    private Integer id;
    private String name;
    private Integer quantity;
    private Integer version;

    public Product() {
    }

    public Product(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Product(Integer id, String name, Integer quantity) {
        setId(id);
        this.name = name;
        this.quantity = quantity;
    }

    public Product(Integer id, String name, Integer quantity, Integer version) {
        setId(id);
        this.name = name;
        this.quantity = quantity;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}



