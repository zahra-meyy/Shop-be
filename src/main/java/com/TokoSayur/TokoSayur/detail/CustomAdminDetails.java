package com.TokoSayur.TokoSayur.detail;

import com.TokoSayur.TokoSayur.model.Admin;
import com.TokoSayur.TokoSayur.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAdminDetails implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ambil Admin berdasarkan username dari database
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin tidak ditemukan dengan username: " + username));

        // Kembalikan objek User untuk digunakan oleh Spring Security
        return new User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.emptyList()  // Anda bisa menambahkan authorities jika diperlukan
        );
    }
}
