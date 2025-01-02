package org.example.teamshop.service.AdminService;

import org.example.teamshop.dto.AdminDto;
import org.example.teamshop.model.Admin;
import org.example.teamshop.request.CreateAdminRequest;
import org.example.teamshop.request.UpdateAdminRequest;

public interface IAdminService {

    Admin findAdminById(Long id);

    AdminDto addAdmin(CreateAdminRequest createdAdmin);

    AdminDto updateAdmin(UpdateAdminRequest updatedAdmin, Long id);

    void deleteAdmin(Long id);
}
