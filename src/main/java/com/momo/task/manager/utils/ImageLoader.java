package com.momo.task.manager.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ImageLoader {

    public byte[] loadImage(String imagePath) {
        try {
            // Load the image using ClassPathResource without the 'src/main/resources' prefix
            Resource resource = new ClassPathResource(imagePath);

            // Read the content of the image
            return resource.getInputStream().readAllBytes();

        } catch (Exception e) {
            // Handle exceptions, e.g., log or throw custom exception
            e.printStackTrace();
            return new byte[0];
        }
    }
}
