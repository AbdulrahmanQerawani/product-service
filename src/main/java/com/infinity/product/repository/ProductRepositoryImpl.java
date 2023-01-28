package com.infinity.product.repository;

import com.infinity.product.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        // Build SimpleJdbcInsert object from the specified data source
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("products").usingGeneratedKeyColumns("id");
    }


    @Override
    public Optional<Product> findById(Integer id) {
        try {
            Product product = (Product) jdbcTemplate.query("select * from products where id = ?",
                    (rs, rowNum) -> {
                        Product p = new Product();
                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setQuantity(rs.getInt("quantity"));
                        p.setVersion(rs.getInt("version"));
                        return p;
                    }, new Object[]{id});
            return Optional.of(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {

        return jdbcTemplate.query("select * from products",
                (rs, rowNum) -> {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setVersion(rs.getInt("version"));
                    return product;
                }
        );
    }

    @Override
    public Boolean update(Product product) {
        try {
            return jdbcTemplate.update("update products set name = ?, quantity = ?, version = ? where id = ? ",
                    product.getName(),product.getQuantity(), product.getVersion(), product.getId()
                    )== 1;
        }catch (DataAccessException e){
            return false;
        }
    }

    @Override
    public Product save(Product product) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", product.getName());
        parameters.put("quantity", product.getName());
        parameters.put("version", product.getName());
        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        log.info("Inserting product into database, generated key is: {}.", id);
        product.setId(id.intValue());
        return product;

    }

    @Override
    public Boolean delete(Integer id) {
        return jdbcTemplate.update("delete from products where id = ",id) == 1;
    }
}
