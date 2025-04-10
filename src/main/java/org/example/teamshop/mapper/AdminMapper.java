package org.example.teamshop.mapper;

import org.example.teamshop.dto.AdminDto;
import org.example.teamshop.model.Admin;
import org.example.teamshop.request.CreateAdminRequest;
import org.example.teamshop.request.UpdateAdminRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin createAdminRequestToAdmin(CreateAdminRequest createAdminRequest);

    AdminDto adminToAdminDto(Admin admin);

    void updateAdminRequestToAdmin(UpdateAdminRequest updateAdminRequest, @MappingTarget Admin admin);
}
