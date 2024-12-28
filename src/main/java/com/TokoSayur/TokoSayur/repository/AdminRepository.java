package com.TokoSayur.TokoSayur.repository;

import com.TokoSayur.TokoSayur.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);  // Menambahkan metode findByEmail
}
