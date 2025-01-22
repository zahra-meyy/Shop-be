package com.TokoSayur.TokoSayur.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produk")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_sayur", nullable = false)
    private String namaSayur;

    @Column(name = "berat_sayur", columnDefinition = "TEXT")
    private String beratSayur;

    @Column(name = "harga_sayur")
    private BigDecimal hargaSayur;

    @Column(name = "foto_url") // Kolom untuk URL gambar
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    // Default constructor
    public Product() {
    }

    // Parameterized constructor without 'id' (since it's auto-generated)
    public Product(Admin admin, String namaSayur, String beratSayur, BigDecimal hargaSayur, String fotoUrl) {
        this.admin = admin;
        this.namaSayur = namaSayur;
        this.beratSayur = beratSayur;
        this.hargaSayur = hargaSayur;
        this.fotoUrl = fotoUrl;
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for namaSayur
    public String getNamaSayur() {
        return namaSayur;
    }

    public void setNamaSayur(String namaSayur) {
        if (namaSayur == null || namaSayur.isEmpty()) {
            throw new IllegalArgumentException("Nama sayur tidak boleh kosong");
        }
        this.namaSayur = namaSayur;
    }

    // Getter and Setter for beratSayur
    public String getBeratSayur() {
        return beratSayur;
    }

    public void setBeratSayur(String beratSayur) {
        this.beratSayur = beratSayur;
    }

    // Getter and Setter for hargaSayur
    public BigDecimal getHargaSayur() {
        return hargaSayur;
    }

    public void setHargaSayur(BigDecimal hargaSayur) {
        if (hargaSayur.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Harga sayur tidak boleh negatif");
        }
        this.hargaSayur = hargaSayur;
    }

    // Getter and Setter for fotoUrl
    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    // Getter and Setter for admin
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin tidak boleh null");
        }
        this.admin = admin;
    }
}
