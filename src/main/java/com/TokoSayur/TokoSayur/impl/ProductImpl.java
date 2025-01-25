package com.TokoSayur.TokoSayur.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.exception.NotFoundException;
import com.TokoSayur.TokoSayur.model.Admin;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.repository.AdminRepository;
import com.TokoSayur.TokoSayur.repository.ProductRepository;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {

    private static final String BASE_URL = "https://s3.lynk2.co/api/s3";
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public ProductImpl(ProductRepository productRepository, AdminRepository adminRepository) {
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllByAdmin(Long idAdmin) {
        return productRepository.findByAdminId(idAdmin);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductDTO addProduct(Long idAdmin, ProductDTO productDTO) {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin not found"));

        Product product = new Product();
        product.setAdmin(admin);
        product.setNamaSayur(productDTO.getNamaSayur());
        product.setBeratSayur(productDTO.getBeratSayur());
        product.setHargaSayur(productDTO.getHargaSayur());
        product.setFotoUrl(productDTO.getFotoUrl());

        Product savedProduct = productRepository.save(product);

        ProductDTO result = new ProductDTO();
        result.setId(savedProduct.getId());  // Set the ID from the saved product
        result.setNamaSayur(savedProduct.getNamaSayur());
        result.setBeratSayur(savedProduct.getBeratSayur());
        result.setHargaSayur(savedProduct.getHargaSayur());
        result.setFotoUrl(savedProduct.getFotoUrl());

        return result;
    }

    @Override
    public ProductDTO updateProduct(Long id, Long idAdmin, ProductDTO productDTO) throws IOException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan"));

        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        // Update atribut produk
        existingProduct.setAdmin(admin);
        existingProduct.setNamaSayur(productDTO.getNamaSayur());
        existingProduct.setBeratSayur(productDTO.getBeratSayur());
        existingProduct.setHargaSayur(productDTO.getHargaSayur());

        // Update foto jika ada
        if (productDTO.getFotoUrl() != null && !productDTO.getFotoUrl().isEmpty()) {
            existingProduct.setFotoUrl(productDTO.getFotoUrl());
        }

        Product updatedProduct = productRepository.save(existingProduct);

        ProductDTO result = new ProductDTO();
        result.setId(updatedProduct.getId());
        result.setNamaSayur(updatedProduct.getNamaSayur());
        result.setBeratSayur(updatedProduct.getBeratSayur());
        result.setHargaSayur(updatedProduct.getHargaSayur());
        result.setFotoUrl(updatedProduct.getFotoUrl());

        return result;
    }


    @Override
    public String uploadFoto(MultipartFile file) throws IOException {
        String uploadUrl = BASE_URL + "/uploadFoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Failed to upload file: " + response.getStatusCode());
        }
    }

    @Override
    public String editUploadFoto(Long id, MultipartFile file) throws IOException {
        String editUrl = BASE_URL + "/editUploadFoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        body.add("fileId", id.toString());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(editUrl, HttpMethod.PUT, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Failed to update file: " + response.getStatusCode());
        }
    }

    private String extractFileUrlFromResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(responseBody);
        JsonNode dataNode = jsonResponse.path("data");
        return dataNode.path("url_file").asText();
    }

    @Override
    public void deleteProduct(Long id) throws IOException {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IOException("Product not found with ID: " + id);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id); // This should return the product by ID
    }

    @Override
    public ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO) throws IOException {
        return null;
    }

    @Override
    public ProductDTO editProductDTO(Long id, Long idAdmin, MultipartFile file) {
        return null;
    }
}