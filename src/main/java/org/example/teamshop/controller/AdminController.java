package org.example.teamshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.ResourceNotFoundException;
import org.example.teamshop.dto.AdminDto;
import org.example.teamshop.request.CreateAdminRequest;
import org.example.teamshop.request.UpdateAdminRequest;
import org.example.teamshop.response.ApiResponse;
import org.example.teamshop.service.AdminService.IAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/admin")
public class AdminController {
    private final IAdminService adminService;

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getAdmin(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).
                body(new ApiResponse("", adminService.findAdminById(id)));
    }

    @PostMapping
    public ResponseEntity<AdminDto> addAdmin(@Valid @RequestBody CreateAdminRequest request){
        AdminDto adminDto = adminService.addAdmin(request);
        return new ResponseEntity<>(adminDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@Valid @RequestBody UpdateAdminRequest request, @PathVariable Long id){
        AdminDto adminDto = adminService.updateAdmin(request,id);
        return new ResponseEntity<>(adminDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdmin(@PathVariable Long id){
        adminService.deleteAdmin(id);
    }

}
