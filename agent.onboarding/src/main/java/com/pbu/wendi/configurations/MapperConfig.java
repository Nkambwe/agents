package com.pbu.wendi.configurations;

import com.pbu.wendi.utils.enums.Gender;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapperBean() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom converter for Gender enum to int
        Converter<Gender, Integer> convertToInt = context -> context.getSource().ordinal();
        Converter<Integer, Gender> convertToGender = context -> Gender.values()[context.getSource()];
        modelMapper.addConverter(convertToInt, Gender.class, Integer.class);
        modelMapper.addConverter(convertToGender, Integer.class, Gender.class);

        return modelMapper;
    }
}
