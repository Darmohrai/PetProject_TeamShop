package org.example.teamshop.mapper;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.AdminDto;
import org.example.teamshop.model.Admin;
import org.example.teamshop.request.CreateAdminRequest;
import org.example.teamshop.request.UpdateAdminRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminMapper {
    private final ModelMapper modelMapper;

    public Admin createAdminRequestToAdmin(CreateAdminRequest createAdminRequest){
        return modelMapper.map(createAdminRequest, Admin.class);
    };

    public AdminDto adminToAdminDto(Admin admin){
        return modelMapper.map(admin, AdminDto.class);
    };

    public void updateAdminRequestToAdmin(UpdateAdminRequest updateAdminRequest, Admin admin){
        if (updateAdminRequest.getEmail() != null) {
            admin.setEmail(updateAdminRequest.getEmail());
        }
        if (updateAdminRequest.getPhone() != null) {
            admin.setPhone(updateAdminRequest.getPhone());
        }
    };
}
