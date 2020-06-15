package org.example.server.config;

import org.example.server.service.SensorService;
import org.example.server.service.SensorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({@PropertySource(value = "classpath:local/db.properties"),
})
public class AppConfig {
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public static SensorService StudentService() {
    return new SensorServiceImpl();
  }
}