package com.bektz.dataplatformsoar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={JdbcTemplateAutoConfiguration.class,DataSourceAutoConfiguration.class})
public class DataPlatformSoarApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPlatformSoarApplication.class, args);
	}

}
