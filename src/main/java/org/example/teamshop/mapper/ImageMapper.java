package org.example.teamshop.mapper;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ImageDto;
import org.example.teamshop.model.Image;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDto toImageDto(Image image);

    Image toImageFromMultipartFile(MultipartFile file) throws IOException;
}
