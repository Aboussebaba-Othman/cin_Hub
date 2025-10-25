package com.cinhub.mapper;

import com.cinhub.dto.DirectorCreateDTO;
import com.cinhub.dto.DirectorDTO;
import com.cinhub.entity.Director;
import org.springframework.stereotype.Component;

@Component
public class DirectorMapper {


    public DirectorDTO toDTO(Director director) {
        if (director == null) {
            return null;
        }

        return DirectorDTO.builder()
                .idDirector(director.getIdDirector())
                .firstName(director.getFirstName())
                .lastName(director.getLastName())
                .nationality(director.getNationality())
                .birthDate(director.getBirthDate())
                .biography(director.getBiography())
                .fullName(director.getFullName())
                .build();
    }


    public Director toEntity(DirectorCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        return Director.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .nationality(dto.getNationality())
                .birthDate(dto.getBirthDate())
                .biography(dto.getBiography())
                .build();
    }
}