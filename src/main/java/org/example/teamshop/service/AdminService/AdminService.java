package org.example.teamshop.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.ResourceNotFoundException;
import org.example.teamshop.dto.AdminDto;
import org.example.teamshop.mapper.AdminMapper;
import org.example.teamshop.model.Admin;
import org.example.teamshop.repository.AdminRepository;
import org.example.teamshop.request.CreateAdminRequest;
import org.example.teamshop.request.UpdateAdminRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;

    private final AdminMapper adminMapper;

    @Override
    public Admin getAdminById(Long id){
        return adminRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Admin with id=" + id + " not exists"));
    }

    @Override
    @Transactional
    public AdminDto addAdmin(CreateAdminRequest createdAdmin) {
        Admin newAdmin = adminMapper.createAdminRequestToAdmin(createdAdmin);
        adminRepository.save(newAdmin);
        return adminMapper.adminToAdminDto(newAdmin);
    }

    @Override
    @Transactional
    public AdminDto updateAdmin(UpdateAdminRequest updatedAdmin, Long id) {
        Admin admin = getAdminById(id);
        adminMapper.updateAdminRequestToAdmin(updatedAdmin,admin);
        adminRepository.save(admin);
        return adminMapper.adminToAdminDto(admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id){
        Admin admin = getAdminById(id);
        adminRepository.delete(admin);
    }
}
