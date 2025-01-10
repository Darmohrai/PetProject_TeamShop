package org.example.teamshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ImageDto;
import org.example.teamshop.model.Image;
import org.example.teamshop.response.ApiResponse;
import org.example.teamshop.service.Image.IImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/images/")
public class ImageController {
    private final IImageService imageService;

    @GetMapping("image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") Long id) {
        Image image = imageService.getImage(id);
        Resource resource = new ByteArrayResource(image.getData());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getType())).body(resource);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDto addImage(@RequestParam MultipartFile image,
                             @RequestParam Long productId) {
        return imageService.uploadImage(image, productId);
    }

    @PutMapping("{imageId}")
    public ImageDto updateImage(@RequestParam MultipartFile image,
                                @PathVariable Long imageId) {
        return imageService.updateImage(image, imageId);
    }

    @DeleteMapping("{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
    }
}
