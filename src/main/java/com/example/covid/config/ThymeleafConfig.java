package com.example.covid.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@RequiredArgsConstructor
@Configuration
public class ThymeleafConfig {

    private final Thymeleaf3Properties thymeleaf3Properties;

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver (
            SpringResourceTemplateResolver defaultTemplateResolver
     ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }
    @RequiredArgsConstructor
    @Getter
    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties {
        private final boolean decoupledLogic;
    }
}
