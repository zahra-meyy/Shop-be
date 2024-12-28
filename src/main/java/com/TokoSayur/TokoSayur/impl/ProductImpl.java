package com.TokoSayur.TokoSayur.impl;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.exception.NotFoundException;
import com.TokoSayur.TokoSayur.model.Admin;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.repository.ProductRepository;
import com.TokoSayur.TokoSayur.repository.AdminRepository;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {  // Implement ProductService

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

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
    public ProductDTO tambahProductDTO(Long idAdmin, ProductDTO productDTO) {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        Product data = new Product();
        data.setAdmin(admin);
        data.setNamaSayur(productDTO.getNamaSayur());
        data.setBeratSayur(productDTO.getBeratSayur().trim());


        // Konversi String ke BigDecimal untuk hargaNovel
        try {
            data.setHargaSayur(productDTO.getHargaSayur());  // Menggunakan BigDecimal langsung
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga novel tidak valid: " + productDTO.getHargaSayur());
        }

        Product savedProduct = productRepository.save(data);

        ProductDTO result = new ProductDTO();
        result.setId(savedProduct.getId());
        result.setIdAdmin(admin.getId());
        result.setNamaSayur(savedProduct.getNamaSayur());
        result.setBeratSayur(savedProduct.getBeratSayur());
        result.setHargaSayur(savedProduct.getHargaSayur()); // Menggunakan BigDecimal langsung
        return result;
    }

    @Override
    public ProductDTO editProductDTO(Long id, ProductDTO productDTO) throws IOException {
        return null;
    }

    @Override
    public ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO) throws IOException {
        Product existingData = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan"));

        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        existingData.setAdmin(admin);
        existingData.setNamaSayur(productDTO.getNamaSayur());
        existingData.setBeratSayur(productDTO.getBeratSayur());

        try {
            existingData.setHargaSayur(productDTO.getHargaSayur());  // Menggunakan BigDecimal langsung
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga novel tidak valid: " + productDTO.getHargaSayur());
        }

        Product updatedProduct = productRepository.save(existingData);

        ProductDTO result = new ProductDTO();
        result.setId(updatedProduct.getId());
        result.setIdAdmin(admin.getId());
        result.setNamaSayur(updatedProduct.getNamaSayur());
        result.setBeratSayur(updatedProduct.getBeratSayur());
        result.setHargaSayur(updatedProduct.getHargaSayur());

        return result;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
