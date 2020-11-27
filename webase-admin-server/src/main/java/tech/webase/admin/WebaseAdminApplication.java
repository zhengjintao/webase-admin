package tech.webase.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author cjbi
 */
@SpringBootApplication
@EnableOpenApi
@MapperScan(basePackages = "tech.webase.admin.mapper")
public class WebaseAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebaseAdminApplication.class, args);
    }
}
