package org.example.teamshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.teamshop.model.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
