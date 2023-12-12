package pl.edu.agh.to2.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.edu.agh.to2.backend.thumbnail.ImageScaler;

@Configuration
public class AppConfig {

    @Bean
    @Scope("prototype")
    public ImageScaler imageScaler(){
        return new ImageScaler();
    }
}
