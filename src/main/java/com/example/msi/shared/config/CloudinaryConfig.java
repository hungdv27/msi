package com.example.msi.shared.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
  @Bean
  public Cloudinary cloudinary() {
    Cloudinary c = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dvw9k1xrp",
        "api_key", "923827835631577",
        "api_secret", "SVJc_TyhjvcReBbouRDGcxvTmBk",
        "secure", true
    ));
    return c;
  }
}
