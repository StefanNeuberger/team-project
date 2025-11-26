package com.team18.backend.config.web;

import com.team18.backend.model.enums.OwnerType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class OwnerTypeConverterConfig {

    /**
     * Dieser Converter ermöglicht die Umwandlung eines String-Werts
     * in den Enum-Typ OwnerType, unabhängig von Groß- und Kleinschreibung.
     * Dadurch akzeptiert der Controller sowohl "PRODUCT", "product" als auch "Product".
     */
    @Bean
    public Converter<String, OwnerType> ownerTypeConverter() {
        return source -> OwnerType.valueOf(source.toUpperCase());
    }
}
