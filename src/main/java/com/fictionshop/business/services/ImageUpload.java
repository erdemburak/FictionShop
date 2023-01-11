package com.fictionshop.business.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUpload {

    // Product images are shaped in predetermined sizes to be used within the site.
    public static void imageUpload(String imageFileName, MultipartFile imageFile) throws IOException {
        String uploadDirectory = "src/main/resources/static/images/fictionshop/product";
        saveFile(uploadDirectory, imageFileName, imageFile);
        String inputImagePath = "src/main/resources/static/images/fictionshop/product/" + imageFileName;
        String outputBigImagePath = "src/main/resources/static/images/fictionshop/product/big/" + imageFileName;
        String outputCartImagePath = "src/main/resources/static/images/fictionshop/product/cart/" + imageFileName;
        String outputDetailImagePath = "src/main/resources/static/images/fictionshop/product/detail/" + imageFileName;
        String outputDetailBigImagePath = "src/main/resources/static/images/fictionshop/product/detailBig/" + imageFileName;

        try{
            double percent = 2;
            ImageResizer.resize(inputImagePath,outputBigImagePath,percent);
            double percentCart = 0.25;
            ImageResizer.resize(inputImagePath,outputCartImagePath,percentCart);
            double percentDetail = 1.5;
            ImageResizer.resize(inputImagePath,outputDetailImagePath,percentDetail);
            ImageResizer.resize(inputImagePath,outputDetailBigImagePath, 410,546);
        }catch (IOException exception){
            System.out.println("Error resizing the image.");
            exception.printStackTrace();
        }
    }

    public static void saveFile(String uploadDirectory, String fileName, MultipartFile file) throws IOException{
        Path path = Paths.get(uploadDirectory);

        try(InputStream inputStream = file.getInputStream()){
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ioe){
            throw new IOException("Could not save image file : " + fileName, ioe);
        }

    }
}
