package com.TokoSayur.TokoSayur.DTO;

import java.math.BigDecimal;

public class ProductDTO {

    private String namaSayur;
    private String beratSayur;
    private BigDecimal hargaSayur; // Gunakan BigDecimal untuk harga
    private String fotoUrl;

    // Constructor untuk memudahkan pembuatan objek dengan parameter
    public ProductDTO(String namaSayur, String beratSayur, BigDecimal hargaSayur, String fotoUrl) {
        this.namaSayur = namaSayur;
        this.beratSayur = beratSayur;
        this.hargaSayur = hargaSayur;
        this.fotoUrl = fotoUrl;
    }

    public ProductDTO() {

    }

    // Getter dan Setter
    public String getNamaSayur() {
        return namaSayur;
    }

    public void setNamaSayur(String namaSayur) {
        this.namaSayur = namaSayur;
    }

    public String getBeratSayur() {
        return beratSayur;
    }

    public void setBeratSayur(String beratSayur) {
        this.beratSayur = beratSayur;
    }

    public BigDecimal getHargaSayur() {
        return hargaSayur;
    }

    public void setHargaSayur(BigDecimal hargaSayur) {
        this.hargaSayur = hargaSayur;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    // Menambahkan metode toString() untuk debug dan logging
    @Override
    public String toString() {
        return "ProductDTO{" +
                "namaSayur='" + namaSayur + '\'' +
                ", beratSayur='" + beratSayur + '\'' +
                ", hargaSayur=" + hargaSayur +
                ", fotoUrl='" + fotoUrl + '\'' +
                '}';
    }

    public void setIdAdmin(Long id) {
    }

    public void setId(Long id) {
    }
}
