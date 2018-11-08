package com.bektz.dataplatformsoar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataPlatformSoarApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPlatformSoarApplication.class, args);
	}

//	@Bean
//	public static PropertySourcesPlaceholderConfigurer properties() {
//		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
//		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//		yaml.setResources(new ClassPathResource[]{
//				new ClassPathResource("soar.yml")
//		});
//		pspc.setProperties(yaml.getObject());
//		return pspc;
//	}
}
