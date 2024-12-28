package com.TokoSayur.TokoSayur.service;


import com.TokoSayur.TokoSayur.DTO.PasswordDTO;
import com.TokoSayur.TokoSayur.model.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminService {

    Admin registerAdmin(Admin admin);

    Admin getById(Long id);

    Optional<Admin> findByAdminname();

    List<Admin> getAll();

    Admin edit(Long id, Admin admin);

    Admin putPasswordAdmin(PasswordDTO passwordDTO, Long id);

    Map<String, Boolean> delete(Long id);

}