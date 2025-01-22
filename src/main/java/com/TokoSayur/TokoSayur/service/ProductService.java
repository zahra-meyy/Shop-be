package com.TokoSayur.TokoSayur.service;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProduct();

    List<Product> getAllByAdmin(Long idAdmin);

    Optional<Product> getById(Long id);

    ProductDTO tambahProductDTO(Long idAdmin, ProductDTO productDTO);

    ProductDTO editProductDTO(Long id, ProductDTO productDTO) throws IOException;

    Optional<Product> getProductById(Long id);

    ProductDTO addProductDTO(Long idAdmin, @NotNull ProductDTO productDTO);

    ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO) throws IOException;

    ProductDTO tambahProductDTO(Long idAdmin, ProductDTO productDTO, MultipartFile file) throws IOException;

    ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO, MultipartFile file) throws IOException;

    String editUploadFoto(Long id, MultipartFile file) throws IOException;

    void deleteProduct(Long id) throws IOException;

    String uploadFoto(MultipartFile file);

    // Add the missing methods to the interface
    Product save(Product product);

    String uploadProductImage(MultipartFile file, Long productId) throws IOException;
}
