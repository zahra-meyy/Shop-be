package com.TokoSayur.TokoSayur.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/all")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> productList = productService.getAllProduct();
        if (productList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Handle case when no data found
        }
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/product/getAllByAdmin/{idAdmin}")
    public ResponseEntity<List<Product>> getAllByAdmin(@PathVariable Long idAdmin) {
        List<Product> productList = productService.getAllByAdmin(idAdmin);
        if (productList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Handle case when no data found for admin
        }
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/product/tambah/{idAdmin}")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable Long idAdmin,
            @RequestParam("product") String productJson,
            @RequestParam("file") MultipartFile file) throws IOException {
        // Convert the product JSON string to ProductDTO
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
        // Upload the photo and get the photo URL from ProductImpl
        String fotoUrl = productService.uploadFoto(file);  // Call the uploadFoto from the service implementation
        // Set the photo URL in the DTO
        productDTO.setFotoUrl(fotoUrl);
        // Save the product with the photo URL
        ProductDTO savedProduct = productService.addProductDTO(idAdmin, productDTO);
        // Log to ensure the fotoUrl is set correctly
        System.out.println("Saved Product: " + savedProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/product/editById/{id}")
    public ResponseEntity<ProductDTO> editProduct(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            // Edit product without photo
            @RequestParam(required = false) MultipartFile file,
            @RequestParam String product) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(product, ProductDTO.class);
        // If a new file is provided, upload it and update the fotoUrl
        if (file != null) {
            String fotoUrl = productService.editUploadFoto(id, file);
            productDTO.setFotoUrl(fotoUrl);
        }
        // Edit the other product fields without photo
        ProductDTO updatedProduct = productService.editProductDTO(id, idAdmin, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // No content response for successful deletion
    }
}
