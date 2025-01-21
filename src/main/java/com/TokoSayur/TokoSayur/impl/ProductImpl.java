package com.TokoSayur.TokoSayur.impl;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.exception.NotFoundException;
import com.TokoSayur.TokoSayur.model.Admin;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.repository.ProductRepository;
import com.TokoSayur.TokoSayur.repository.AdminRepository;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private static final String BASE_URL = "https://s3.lynk2.co/api/s3"; // URL dasar untuk penyimpanan gambar

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
        return null;
    }

    @Override
    public ProductDTO editProductDTO(Long id, ProductDTO productDTO) throws IOException {
        return null;
    }

    @Override
    public ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO) throws IOException {
        return null;
    }

    @Override
    public ProductDTO tambahProductDTO(Long idAdmin, ProductDTO productDTO, MultipartFile file) throws IOException {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        // Membuat objek produk baru
        Product data = new Product();
        data.setAdmin(admin);
        data.setNamaSayur(productDTO.getNamaSayur());
        data.setBeratSayur(productDTO.getBeratSayur().trim());

        try {
            data.setHargaSayur(productDTO.getHargaSayur());  // Menggunakan BigDecimal langsung
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga sayur tidak valid: " + productDTO.getHargaSayur());
        }

        // Upload gambar jika ada
        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadProductImage(file, data.getId());  // Mengunggah gambar dan mendapatkan URL
            data.setImage(imageUrl);  // Menyimpan URL gambar ke dalam field image
        }

        // Simpan produk baru ke database
        Product savedProduct = productRepository.save(data);

        // Membuat DTO untuk hasil yang dikembalikan
        ProductDTO result = new ProductDTO();
        result.setId(savedProduct.getId());
        result.setIdAdmin(admin.getId());
        result.setNamaSayur(savedProduct.getNamaSayur());
        result.setBeratSayur(savedProduct.getBeratSayur());
        result.setHargaSayur(savedProduct.getHargaSayur());
        result.setImage(savedProduct.getImage());  // Menambahkan URL gambar ke DTO

        return result;
    }

    @Override
    public ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO, MultipartFile file) throws IOException {
        // Cek apakah produk dengan ID yang diberikan ada
        Product existingData = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan"));

        // Temukan admin berdasarkan ID yang diberikan
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        // Update data produk
        existingData.setAdmin(admin);
        existingData.setNamaSayur(productDTO.getNamaSayur());
        existingData.setBeratSayur(productDTO.getBeratSayur());

        try {
            existingData.setHargaSayur(productDTO.getHargaSayur());  // Menggunakan BigDecimal langsung
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga sayur tidak valid: " + productDTO.getHargaSayur());
        }

        // Upload gambar jika ada
        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadProductImage(file, existingData.getId());  // Mengunggah gambar dan mendapatkan URL
            existingData.setImage(imageUrl);  // Menyimpan URL gambar ke dalam field image
        }

        // Simpan produk yang sudah diubah ke database
        Product updatedProduct = productRepository.save(existingData);

        // Membuat DTO untuk hasil yang dikembalikan
        ProductDTO result = new ProductDTO();
        result.setId(updatedProduct.getId());
        result.setIdAdmin(admin.getId());
        result.setNamaSayur(updatedProduct.getNamaSayur());
        result.setBeratSayur(updatedProduct.getBeratSayur());
        result.setHargaSayur(updatedProduct.getHargaSayur());
        result.setImage(updatedProduct.getImage());  // Menambahkan URL gambar ke DTO

        return result;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Menambahkan method untuk meng-upload gambar produk
    public String uploadProductImage(MultipartFile file, Long productId) throws IOException {
        // Cek apakah file tidak kosong
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File tidak boleh kosong");
        }

        // Menyusun nama file dengan ID produk untuk menghindari duplikasi
        String fileName = productId + "_" + file.getOriginalFilename();

        // Tentukan path penyimpanan lokal atau S3
        String directory = "/path/to/your/upload/directory"; // Sesuaikan dengan direktori Anda
        File tempFile = new File(directory, fileName);

        // Pastikan direktori tempat penyimpanan ada
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Simpan file di server lokal
        file.transferTo(tempFile);

        // Setelah file disimpan, dapatkan URL untuk akses gambar
        String fileUrl = BASE_URL + "/uploads/" + fileName;  // Path gambar di URL yang bisa diakses

        return fileUrl; // Mengembalikan URL gambar yang di-upload
    }
}
