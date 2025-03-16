package org.example.teamshop.service.SecurityServices.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.model.Admin;
import org.example.teamshop.model.Client;
import org.example.teamshop.repository.AdminRepository;
import org.example.teamshop.repository.ClientRepository;
import org.example.teamshop.securityModel.SecurityAdmin;
import org.example.teamshop.securityModel.SecurityClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(userEmail);
        if (admin != null) {
            return new SecurityAdmin(admin);
        }

        Client client = clientRepository.findByEmail(userEmail);
        if (client != null) {
            return new SecurityClient(client);
        }

        throw new UsernameNotFoundException("User not found with email: " + userEmail);
    }
}
