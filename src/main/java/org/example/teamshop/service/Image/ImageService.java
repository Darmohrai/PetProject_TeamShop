package org.example.teamshop.service.Image;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.FailedOperationException;
import org.example.teamshop.Exception.ResourceNotFoundException;
import org.example.teamshop.dto.ImageDto;
import org.example.teamshop.mapper.ImageMapper;
import org.example.teamshop.model.Image;
import org.example.teamshop.repository.ImageRepository;
import org.example.teamshop.service.ProductService.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final ImageMapper imageMapper;

    private Image findImageById(Long id) {
        return imageRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public ImageDto uploadImage(MultipartFile file, Long productId) {
        try {
            var product = productService.getProductById(productId);
            var image = imageMapper.toImageFromMultipartFile(file);

            image.setProduct(product);
            image = imageRepository.save(image);
            image.setUrl("/dobry/ounivets/shop/images/image/" + image.getId());

            return imageMapper.toImageDto(imageRepository.save(image));
        } catch (IOException e) {
            throw new FailedOperationException("Image failed to upload");
        }
    }

    @Override
    public ImageDto updateImage(MultipartFile file, Long imageId) {
        try {
            var image = findImageById(imageId);
            image = imageMapper.toImageFromMultipartFile(file);

            return imageMapper.toImageDto(imageRepository.save(image));
        } catch (IOException e) {
            throw new FailedOperationException("Image failed to upload");
        }
    }

    @Override
    public void deleteImage(Long imageId) {
        var image = findImageById(imageId);
        imageRepository.delete(image);
    }
}
