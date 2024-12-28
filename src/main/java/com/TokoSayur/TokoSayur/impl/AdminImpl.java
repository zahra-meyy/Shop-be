package com.TokoSayur.TokoSayur.impl;

import com.TokoSayur.TokoSayur.DTO.PasswordDTO;
import com.TokoSayur.TokoSayur.exception.BadRequestException;
import com.TokoSayur.TokoSayur.exception.NotFoundException;
import com.TokoSayur.TokoSayur.model.Admin;
import com.TokoSayur.TokoSayur.repository.AdminRepository;
import com.TokoSayur.TokoSayur.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminImpl(AdminRepository adminRepository, PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    @Override
    public Admin registerAdmin(Admin admin) {
        // Check if the email already exists
        Optional<Admin> existingEmail = adminRepository.findByEmail(admin.getEmail());
        if (existingEmail.isPresent()) {
            throw new BadRequestException("Email sudah digunakan");
        }

        // Check if the username already exists
        Optional<Admin> existingUsername = adminRepository.findByUsername(admin.getUsername());
        if (existingUsername.isPresent()) {
            throw new BadRequestException("Username sudah digunakan");
        }

        // Set the role to "ADMIN" by default
        admin.setRole("ADMIN");

        // Encode the password before saving
        admin.setPassword(encoder.encode(admin.getPassword()));

        // Save and return the registered admin
        return adminRepository.save(admin);
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID tidak ditemukan"));
    }

    @Override
    public Optional<Admin> findByAdminname() {
        return Optional.empty();
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin edit(Long id, Admin admin) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin tidak ditemukan"));

        existingAdmin.setUsername(admin.getUsername());
        existingAdmin.setEmail(admin.getEmail());
        return adminRepository.save(existingAdmin);
    }

    @Override
    public Admin putPasswordAdmin(PasswordDTO passwordDTO, Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID tidak ditemukan"));

        boolean isOldPasswordCorrect = encoder.matches(passwordDTO.getOld_password(), admin.getPassword());

        if (!isOldPasswordCorrect) {
            throw new BadRequestException("Password lama tidak sesuai");
        }

        if (!passwordDTO.getNew_password().equals(passwordDTO.getConfirm_new_password())) {
            throw new BadRequestException("Password baru tidak cocok");
        }

        admin.setPassword(encoder.encode(passwordDTO.getNew_password()));
        return adminRepository.save(admin);
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            adminRepository.deleteById(id);
            response.put("deleted", Boolean.TRUE);
        } catch (Exception e) {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }

}