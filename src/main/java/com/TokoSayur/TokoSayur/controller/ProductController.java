package com.TokoSayur.TokoSayur.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/data/product")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> productDTOs  = productService.getAllProduct();
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/data/getAllByAdmin/{idAdmin}")
    public ResponseEntity<List<Product>> getAllByAdmin(@PathVariable Long idAdmin) {
        List<Product> productDTOs  = productService.getAllByAdmin(idAdmin);
        return ResponseEntity.ok(productDTOs );
    }

    @GetMapping("/data/product/{id}")
    public ResponseEntity<Product>  getById(@PathVariable Long id) {
        Optional<Product> product = productService.getById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/data/tambah/{idAdmin}")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable Long idAdmin,
            @RequestParam("product") String productJson,
            @RequestParam("file") MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);

        String fotoUrl = productService.uploadFoto(file);
        productDTO.setFotoUrl(fotoUrl);

        ProductDTO savedProduct = productService.addProduct(idAdmin, productDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/data/edit/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam("idAdmin") Long idAdmin,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("product") String productJson) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);

        if (file != null && !file.isEmpty()) {
            String fotoUrl = productService.uploadFoto(file);
            productDTO.setFotoUrl(fotoUrl);
        }

        ProductDTO updatedProduct = productService.updateProduct(id, idAdmin, productDTO);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/data/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}