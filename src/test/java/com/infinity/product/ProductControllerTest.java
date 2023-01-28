package com.infinity.product;

import com.infinity.product.domain.Product;
import com.infinity.product.service.ProductService;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.infinity.product.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {
    @MockBean
    ProductService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("GET /product/1 - Found")
    void getProductByIdTest() throws Exception {
        // Set up our mocked service
        Product mockProduct = new Product(1, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(service).findById(1);

        // Execute The GET request
        mockMvc.perform(get("/product/{id}", 1))

                // Validate the response code and content
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the header
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Name")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /product/1 - NotFound")
    void getProductByIdNotFoundTest() throws Exception {
        // Set up our mocked service
        doReturn(Optional.empty()).when(service).findById(1);

        // Execute the GET request
        mockMvc.perform(get("/product/{id}", 1))

                // Validate that we get a 404 Not Found response code
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("POST /product - Success")
    void postProductSuccessTest() throws Exception {
        // Set up our mocked service
        Product postProduct = new Product("Product Name", 10);
        Product mockProduct = new Product(1, "Product Name", 10, 1);
        doReturn(mockProduct).when(service).save(any());

        // Execute The POST request
        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(postProduct)))

                // Validate the response code and content
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the header
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Name")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("PUT /product/{id} - Success")
    void productPutSuccessTest() throws Exception {
        // Set up our mocked service
        Product putProduct = new Product("Product Name", 10);
        Product mockProduct = new Product(1, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(service).findById(1);
        doReturn(Boolean.TRUE).when(service).update(any());

        // Execute The PUT request
        mockMvc.perform(put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                // Validate the response code and content
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the header
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/product/1"))

                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product Name")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(2)));

    }

    @Test
    @DisplayName("PUT /product/{id} - version Mismatch")
    void productPutVersionMismatchTest() throws Exception {
        // Set up our mocked service
        Product putProduct = new Product("Product Name", 10);
        Product mockProduct = new Product(1, "Product Name", 10, 2);
        doReturn(Optional.of(mockProduct)).when(service).findById(1);
        doReturn(Boolean.TRUE).when(service).update(any());

        // Execute The PUT request
        mockMvc.perform(put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                // Validate the response code and content
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("PUT /product/{id} - Not Found")
    void productPutNotFoundTest() throws Exception {

        // Set up our mocked service
        Product putProduct = new Product("Product Name", 10);
        doReturn(Optional.empty()).when(service).findById(1);

        // Execute The PUT request
        mockMvc.perform(put("/product/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.IF_MATCH, 1)
                        .content(asJsonString(putProduct)))

                // Validate the response code and content
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("DELETE /product/{id} - Success")
    void productDeleteSuccessTest() throws Exception {
        // Set up our mocked service
        Product mockProduct = new Product(1, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(service).findById(1);
        doReturn(Boolean.TRUE).when(service).delete(any());

        // Execute the DELETE request to Delete product with id: 1
        mockMvc.perform(delete("/product/{id}",1))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE /product/{id} - Not Found")
    void productDeleteNotFoundTest() throws Exception {
        // Set up our mocked service
        doReturn(Optional.empty()).when(service).findById(1);

        // Execute the DELETE request
        mockMvc.perform(delete("/product/{id}",1))
                .andExpect(status().isNotFound());

    }
    @Test
    @DisplayName("DELETE /product/{id} - Failure")
    void productDeleteFailureTest() throws Exception {
        // Set up our mocked service
        Product mockProduct = new Product(1, "Product Name", 10, 1);
        doReturn(Optional.of(mockProduct)).when(service).findById(1);
        doReturn(Boolean.FALSE).when(service).delete(1);

        // Execute the DELETE request to Delete product with id: 1
        mockMvc.perform(delete("/product/{id}",1))
                .andExpect(status().isInternalServerError());
    }
//
//    @Test
//    @DisplayName("POST /product - Fail")
//    void postProductFailTest() throws Exception {
//        // Set up our mocked service
//        Product postProduct = new Product("Product Name", 10);
//        Product mockProduct = new Product(1, "Product Name", 10, 1);
//
//        // Execute the GET request
//        mockMvc.perform(post("/product")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(postProduct)))
//
//                // Validate that we get an Internal Server Error response code
//                .andExpect(status().isInternalServerError());
//
//    }
}
