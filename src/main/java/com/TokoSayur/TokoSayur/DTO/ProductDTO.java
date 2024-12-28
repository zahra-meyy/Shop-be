package com.TokoSayur.TokoSayur.DTO;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private Long idAdmin;
    private String namaSayur;
    private String beratSayur;
    private BigDecimal hargaSayur;
    private String image;  // Menambahkan properti untuk gambar

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {  // Menambahkan setter untuk gambar
        this.image = image;
    }
}
