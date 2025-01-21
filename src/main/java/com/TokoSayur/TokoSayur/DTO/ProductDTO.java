package com.TokoSayur.TokoSayur.DTO;

import java.math.BigDecimal;

public class ProductDTO {
    private String NamaSayur;
    private String BeratSayur;
    private BigDecimal HargaSayur; // Gunakan BigDecimal untuk harga
    private String Image;

    public String getNamaSayur() {
        return NamaSayur;
    }

    public void setNamaSayur(String namaSayur) {
        NamaSayur = namaSayur;
    }

    public String getBeratSayur() {
        return BeratSayur;
    }

    public void setBeratSayur(String beratSayur) {
        BeratSayur = beratSayur;
    }

    public BigDecimal getHargaSayur() {
        return HargaSayur;
    }

    public void setHargaSayur(BigDecimal hargaSayur) {
        HargaSayur = hargaSayur;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public void setIdAdmin(Long id) {
    }

    public void setId(Long id) {

    }

    public void setApiUrl(String s) {
    }
}

// Getters and setters
