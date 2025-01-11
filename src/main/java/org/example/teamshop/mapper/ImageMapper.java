package org.example.teamshop.mapper;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ImageDto;
import org.example.teamshop.model.Image;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ImageMapper {
    private final ModelMapper modelMapper;

    public ImageDto toImageDto(Image image) {
        return modelMapper.map(image, ImageDto.class);
    }

    public Image toImageFromMultipartFile(MultipartFile file) throws IOException {
        var image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setData(file.getBytes());
        return image;
    }
}
