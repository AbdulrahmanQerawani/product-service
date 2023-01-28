package com.infinity.product;

import com.infinity.product.domain.Product;
import com.infinity.product.repository.ProductRepository;
import com.infinity.product.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {
    /***
     * The service that we want to test
     */
    @Autowired
    ProductService productService;
    /***
     * A mock version of ProductRepository which will use in this test
     */
    @MockBean
    ProductRepository repository;

    @Test
    @DisplayName("findById Success Test")
    void finByIdSuccessTest(){
        // Setup our mock
        Product mockProduct = new Product(1,"Product name",10,1);
        doReturn(Optional.of(mockProduct)).when(repository).findById(any());

        // execute the service call
        Optional<Product> returnedProduct = productService.findById(1);

        // assert the response
        assertTrue(returnedProduct.isPresent(),"Product was not found");
        assertSame(mockProduct, returnedProduct.get(),"Products should be the same");
    }

    @Test
    @DisplayName("findById Not Found Test")
    void finByIdNotFoundTest(){
        // Setup our mock
        Product mockProduct = new Product(1,"Product name",10,1);
        doReturn(Optional.empty()).when(repository).findById(any());

        // execute the service call
        Optional<Product> returnedProduct = productService.findById(1);

        // assert the response
        assertFalse(returnedProduct.isPresent(),"Product was found, when it shouldn't be");
    }

    @Test
    @DisplayName("findAll Success Test")
    void finAllSuccessTest(){
        // Setup our mock
        Product mockProduct1 = new Product(1,"Product name 1",10,1);
        Product mockProduct2 = new Product(2,"Product name 2",10,1);
        doReturn(Arrays.asList(mockProduct1,mockProduct2)).when(repository).findAll();
        // execute the service call
        List<Product> returnedProducts = productService.findAll();

        // assert the response
        assertEquals(2, returnedProducts.size(), "findAll should return 2 products");
    }

    @Test
    @DisplayName("save Success Test")
    void saveSuccessTest(){
        // Setup our mock
        Product mockProduct = new Product(1,"Product name",10);
        doReturn(mockProduct).when(repository).save(any());

        // execute the service call
        Product returnedProduct = productService.save(mockProduct);

        // assert the response
        assertNotNull(returnedProduct,"Product should be not null");
        assertEquals(1,returnedProduct.getVersion().intValue(),
                "The version for a new Product should be 1");
    }

    @Test
    @DisplayName("save Failure Test")
    void saveFailureTest(){
        // Setup our mock
        Product mockProduct = new Product(1,"Product name",10,1);
        doReturn(null).when(repository).save(any());

        // execute the service call
        Product returnedProduct = productService.save(mockProduct);

        // assert the response
        assertNotEquals(null,mockProduct,"Returns saved Product, when it shouldn't");
    }

}
