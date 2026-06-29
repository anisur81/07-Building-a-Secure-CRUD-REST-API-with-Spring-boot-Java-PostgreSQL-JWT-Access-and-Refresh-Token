package com.example.restapi.controller;

import com.example.restapi.entity.Product;
import com.example.restapi.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import com.example.restapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.restapi.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }
/*
    @GetMapping
    public List<Product> getAll() {
        return repository.findAll();
    }
*/

@GetMapping
public ResponseEntity<ApiResponse<List<Product>>> getAll() {

    List<Product> products = repository.findAll();

    if (products.isEmpty()) {
        throw new ResourceNotFoundException("No products found");
    }

    return ResponseEntity.ok(
            new ApiResponse<>(
                    "success",
                    "Products retrieved successfully",
                    products
            )
    );
}

	
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<Product>> getById(@PathVariable Long id) {

    Product product = repository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Product not found"));

    return ResponseEntity.ok(

            new ApiResponse<>(

                    "success",

                    "Product retrieved successfully",

                    product
            )
    );
}
	
/*
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow();
    }
*/
/*
    @PostMapping
    public Product create(@RequestBody Product product) {
        return repository.save(product);
    }
*/


@PostMapping
public ResponseEntity<ApiResponse<Product>> create(
        @RequestBody Product product) {

    Product saved = repository.save(product);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                    new ApiResponse<>(
                            "success",
                            "Product created successfully",
                            saved
                    )
            );
}

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id,
                          @RequestBody Product product) {

        Product existing = repository.findById(id)
                .orElseThrow();

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());

        return repository.save(existing);
    }
/*
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
	
	*/
	
@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

    Product product = repository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Product not found"));

    repository.delete(product);

    return ResponseEntity.ok(

            new ApiResponse<>(

                    "success",
                    "Product deleted successfully",
                    null
            )
    );
}
	
}