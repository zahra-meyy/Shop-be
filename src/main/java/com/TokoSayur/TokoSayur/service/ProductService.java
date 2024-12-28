package com.TokoSayur.TokoSayur.service;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProduct();

    List<Product> getAllByAdmin(Long idAdmin);

    Optional<Product> getById(Long id);

    ProductDTO tambahProductDTO(Long idAdmin, ProductDTO productDTO);

    ProductDTO editProductDTO(Long id, ProductDTO productDTO) throws IOException;

    ProductDTO editProductDTO(Long id, Long idAdmin, ProductDTO productDTO) throws IOException;
    void deleteProduct(Long id) throws IOException;
}