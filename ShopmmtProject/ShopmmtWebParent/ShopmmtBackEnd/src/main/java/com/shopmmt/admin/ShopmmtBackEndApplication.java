package com.shopmmt.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopmmt.common.entity", "com.shopmmt.admin.repositories"})
public class ShopmmtBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmmtBackEndApplication.class, args);
	}

}
