package com.shopmmt.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopmmt.common.entity", "com.shopmmt.site.repositories"})
public class ShopmmtFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmmtFrontEndApplication.class, args);
	}

}
