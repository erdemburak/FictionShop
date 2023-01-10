package com.fictionshop.ui.restcontroller;

import com.fictionshop.business.dto.ProductDto;
import com.fictionshop.business.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductRestController {

    @Autowired
    ProductService productService;

    // LIST
    // http://localhost:8080/api/v1/product/list
    @GetMapping("/list")
    public List<ProductDto> getAllProducts(){
        List<ProductDto> productDtoList = productService.getAllProducts();
        return productDtoList;
    }

    // Save
    // http://localhost:8080/api/v1/product/create
    @PostMapping("/create")
    public ProductDto createProduct(@RequestBody ProductDto productDto, MultipartFile imageFile) throws IOException {
        productService.createProduct(productDto,imageFile);
        return productDto;
    }

    // Find
    // http://localhost:8080/api/v1/product/find/id
    @GetMapping("/find/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) throws Throwable {
        ResponseEntity<ProductDto> productDto= productService.getProductById(id);
        return productDto;
    }

    // Update
    // http://localhost:8080/api/v1/product/update/id
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateEmployee(@PathVariable Long id, @RequestBody ProductDto productDto, MultipartFile imageFile) throws Throwable {
        productService.updateProduct(id, productDto, imageFile);
        return ResponseEntity.ok(productDto);
    }

    // Delete
    // http://localhost:8080/api/v1/product/delete/id
    @DeleteMapping("/delete/{id}")
    public Boolean deleteProduct(@PathVariable(name = "id") Long id) throws Throwable {
        productService.deleteProduct(id);
        return true;
    }


}
