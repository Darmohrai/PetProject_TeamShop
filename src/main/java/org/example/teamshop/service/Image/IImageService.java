package org.example.teamshop.service.Image;

import org.example.teamshop.dto.ImageDto;
import org.example.teamshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    Image getImage(Long id);
    ImageDto uploadImage(MultipartFile file, Long productId);
    ImageDto updateImage(MultipartFile file,  Long imageId);
    void deleteImage(Long id);
}
