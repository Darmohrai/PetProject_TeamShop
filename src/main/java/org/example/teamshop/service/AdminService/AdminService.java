package org.example.teamshop.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.ResourceNotFoundException;
import org.example.teamshop.dto.AdminDto;
import org.example.teamshop.mapper.AdminMapper;
import org.example.teamshop.model.Admin;
import org.example.teamshop.repository.AdminRepository;
import org.example.teamshop.request.CreateAdminRequest;
import org.example.teamshop.request.UpdateAdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;

    @Autowired
    private final AdminMapper adminMapper;

    @Override
    public Admin findAdminById(Long id){
        return adminRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Admin with id=" + id + " not exists"));
    }

    @Override
    public AdminDto addAdmin(CreateAdminRequest createdAdmin) {
        Admin newAdmin = adminMapper.createAdminRequestToAdmin(createdAdmin);
        adminRepository.save(newAdmin);
        return adminMapper.adminToAdminDto(newAdmin);
    }

    @Override
    public AdminDto updateAdmin(UpdateAdminRequest updatedAdmin, Long id) {
        Admin admin = findAdminById(id);
        adminMapper.updateAdminRequestToAdmin(updatedAdmin,admin);
        adminRepository.save(admin);
        return adminMapper.adminToAdminDto(admin);
    }

    @Override
    public void deleteAdmin(Long id){
        Admin admin = findAdminById(id);
        adminRepository.delete(admin);
    }
}
