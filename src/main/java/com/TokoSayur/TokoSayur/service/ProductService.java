package com.TokoSayur.TokoSayur.service;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProduct();

    List<Product> getAllByAdmin(Long idAdmin);

    Optional<Product> getById(Long id);

    ProductDTO addProduct(Long idAdmin, ProductDTO productDTO);

    ProductDTO updateProduct(Long id, Long idAdmin, ProductDTO productDTO) throws IOException;

    String editUploadFoto(Long id, MultipartFile file) throws IOException;

    String uploadFoto(MultipartFile file) throws IOException;

    void deleteProduct(Long id) throws IOException;

    Optional<Product> getProductById(Long id);

    // Menambahkan metode editProductDTO
    ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO) throws IOException;

    // Tetap mempertahankan metode dengan MultipartFile jika masih diperlukan
    ProductDTO editProductDTO(Long id, Long idAdmin, MultipartFile file) throws IOException;
}
