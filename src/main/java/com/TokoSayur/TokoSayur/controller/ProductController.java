package com.TokoSayur.TokoSayur.controller;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/data/product")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> productList = productService.getAllProduct();
        if (productList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Handle case when no data found
        }
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/data/getAllByAdmin/{idAdmin}")
    public ResponseEntity<List<Product>> getAllByAdmin(@PathVariable Long idAdmin) {
        List<Product> productList = productService.getAllByAdmin(idAdmin);
        if (productList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Handle case when no data found for user
        }
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/data/product/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Optional<Product> data = productService.getById(id);
        return data.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/data/tambah/{idAdmin}")
    public ResponseEntity<ProductDTO> tambahProduct(
            @PathVariable Long idAdmin,
            @RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.tambahProductDTO(idAdmin, productDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping(value = "/data/editById/{id}")
    public ResponseEntity<ProductDTO> editProduct(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            @RequestBody ProductDTO productDTO) throws IOException { // Menggunakan @RequestBody untuk buketDTO

        // Edit buket tanpa foto
        ProductDTO updatedProduct = productService.editProductDTO(id, idAdmin, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/data/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // No content response for successful deletion
    }
}