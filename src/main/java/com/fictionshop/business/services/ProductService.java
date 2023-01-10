package com.fictionshop.business.services;

import com.fictionshop.business.data.model.PRODUCT_TYPE;
import com.fictionshop.business.dto.ProductDto;
import com.fictionshop.business.data.entity.ProductEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {

    public List<ProductDto> getAllProducts();
    public ProductDto createProduct(ProductDto productDto, MultipartFile imageFile) throws IOException;
    public ResponseEntity<ProductDto> getProductById(Long id) throws Throwable;
    public List<ProductDto> getProductsByType(PRODUCT_TYPE productType);
    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto, MultipartFile imageFile) throws Throwable;
    public Boolean deleteProduct(Long id) throws Throwable;

    public ProductDto EntityToDto(ProductEntity productEntity);
    public ProductEntity DtoToEntity(ProductDto productDto);

}
