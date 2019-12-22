package com.hinstein.android.experiment;

import com.hinstein.android.experiment.test3.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ExperimentApplication extends SpringBootServletInitializer {

    public static com.hinstein.android.experiment.test3.Server server;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ExperimentApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExperimentApplication.class, args);


        server = new Server(2080);
        server.start();

    }

}
