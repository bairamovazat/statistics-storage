package ru.ivmiit.web.config;


import com.vaadin.flow.spring.SpringServlet;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import ru.ivmiit.web.security.config.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan(value = "ru.ivmiit.web")
@Slf4j
@EnableFeignClients(basePackages = "ru.ivmiit.web")
@Import({AppConfiguration.class, SecurityConfig.class})
public class AppInitializer extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error(e.getMessage(), e);
        });
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(AppInitializer.class);
    }

    //Чтобым можно было запускать через war
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }


    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context =
                new AnnotationConfigWebApplicationContext();
        registerConfiguration(context);
        servletContext.addListener(
                new ContextLoaderListener(context));

        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", new SpringServlet(context, true));
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");
    }

    private void registerConfiguration(AnnotationConfigWebApplicationContext context) {
        // register your configuration classes here
    }
}