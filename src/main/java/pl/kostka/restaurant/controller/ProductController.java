package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.repository.ProductRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class ProductController {

    private ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @GetMapping("/products")
    public List<Product> getProduct() {
        return productRepository.findAll();
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }


    @PutMapping("/products/{productId}")
    public Product updateProduct(@PathVariable(value = "productId")  Long productId, @Valid @RequestBody Product productRequest) {
        return productRepository.findById(productId).map(product -> {
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setType(productRequest.getType());
            return productRepository.save(product);
        }).orElseThrow(() -> new ResourceNotFoundException("ProductId "+ productId + " not found"));
    }

    @PutMapping("/products/{productId}/image/{imageId}")
    public Product updateProductImage(@PathVariable(value = "productId") Long productId,@PathVariable(value = "imageId") Long imageId) {
        return productRepository.findById(productId).map(product -> {
            product.setImageId(imageId);
            return productRepository.save(product);
        }).orElseThrow(() -> new ResourceNotFoundException("ProductId "+ productId + " not found"));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "productId") Long productId) {
        return productRepository.findById(productId).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ProductId "+ productId + " not found"));
    }

}
