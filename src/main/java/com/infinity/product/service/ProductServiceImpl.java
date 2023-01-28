package com.infinity.product.service;

import com.infinity.product.domain.Product;
import com.infinity.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Boolean update(Product product) {
        return repository.update(product);
    }

    @Override
    public Product save(Product product) {
        product.setVersion(1);
        return repository.save(product);
    }

    @Override
    public Boolean delete(Integer id) {
        return repository.delete(id);
    }
}
