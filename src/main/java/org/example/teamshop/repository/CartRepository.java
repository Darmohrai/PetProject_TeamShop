package org.example.teamshop.repository;

import org.example.teamshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByClientId(Long clientId);

    Long findIdByClientId(Long clientId);
}
