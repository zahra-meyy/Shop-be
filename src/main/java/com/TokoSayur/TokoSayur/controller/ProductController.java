package com.TokoSayur.TokoSayur.controller;

import com.TokoSayur.TokoSayur.DTO.ProductDTO;
import com.TokoSayur.TokoSayur.model.Product;
import com.TokoSayur.TokoSayur.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
            @RequestBody ProductDTO productDTO) throws IOException {
        // Logika untuk mengedit produk
        ProductDTO updatedProduct = productService.editProductDTO(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/data/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // No content response for successful deletion
    }

    // Fungsi untuk meng-upload foto produk
    @PostMapping("/data/uploadFoto")
    public ResponseEntity<String> uploadFoto(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileUrl = uploadFotoToS3(multipartFile);
        return ResponseEntity.ok(fileUrl);
    }

    // Fungsi untuk mengirimkan file ke Amazon S3
    private String uploadFotoToS3(MultipartFile multipartFile) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String base_url = "https://s3.lynk2.co/api/s3/absenMasuk"; // URL untuk upload file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", multipartFile.getResource()); // Menambahkan file ke body request

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(base_url, HttpMethod.POST, requestEntity, String.class);
        String fileUrl = extractFileUrlFromResponse(response.getBody()); // Mengambil URL file dari response
        return fileUrl;
    }

    // Fungsi untuk mengekstrak URL file dari response JSON
    private String extractFileUrlFromResponse(String responseBody) {
        // Misalkan response adalah JSON dan memiliki format { "url": "link-file" }
        // Kamu bisa menambahkan kode untuk mengekstrak URL file di sini
        // Contoh parsing menggunakan JSON:
        // JSONObject responseJson = new JSONObject(responseBody);
        // return responseJson.getString("url");

        // Return file URL secara langsung dari response (dalam contoh ini, asumsi format response adalah URL langsung)
        return responseBody; // Ganti dengan parsing JSON jika perlu
    }
}
