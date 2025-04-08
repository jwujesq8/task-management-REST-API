package com.api.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class ModelMapperConfig
 *
 * Configuration class for setting up a {@link ModelMapper} bean in the Spring application context.
 * The {@link ModelMapper} is used for object mapping, allowing the conversion of one Java object to another.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates and returns a {@link ModelMapper} instance as a Spring bean.
     * This bean is used for mapping between different object types, simplifying the process of converting one object
     * to another, especially in scenarios like DTOs to entities or vice versa.
     *
     * @return a new {@link ModelMapper} instance.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
