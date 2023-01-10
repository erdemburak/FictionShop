package com.fictionshop.business.services;

import com.fictionshop.business.dao.ProductDao;
import com.fictionshop.business.data.model.PRODUCT_TYPE;
import com.fictionshop.business.dto.ProductDto;
import com.fictionshop.business.data.entity.ProductEntity;
import com.fictionshop.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ModelMapper modelMapper;


    // List
    // http://localhost:8080/api/v1/product/list
    @GetMapping("/list")
    @Cacheable(value = "product_list")
    @Transactional(readOnly = true)
    @Override
    public List<ProductDto> getAllProducts() {

        List<ProductDto> listDto = new ArrayList<>();
        Iterable<ProductEntity> entityList = productDao.findAll();
        for (ProductEntity entity : entityList){
            ProductDto productDto = EntityToDto(entity);
            listDto.add(productDto);
        }
        return listDto;
    }

    // Save
    // http://localhost:8080/api/v1/product/create
    @PostMapping("/create")
    @CacheEvict(allEntries = true, cacheNames = { "product_list", "products" })
    @Transactional( isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    @Override
    public ProductDto createProduct(@RequestBody ProductDto productDto, MultipartFile imageFile) throws IOException {
        ProductEntity productEntity = DtoToEntity(productDto);

        String imageFileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        productEntity.setProductImageName(imageFileName);
        productDao.save(productEntity);

        ImageUpload.imageUpload(imageFileName,imageFile);

        return productDto;
    }



    // Find
    // http://localhost:8080/api/v1/product/find/id
    @GetMapping("/find/{id}")
    @CachePut(value = "products", key = "#id")
    @Override
    public ResponseEntity<ProductDto> getProductById(@PathVariable(name = "id") Long id) throws Throwable {
        ProductEntity productEntity = productDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not exist with id --> " + id));
        ProductDto productDto = EntityToDto(productEntity);
        return ResponseEntity.ok(productDto);
    }


    // FindByType
    // http://localhost:8080/api/v1/product/find/id
    @GetMapping("/type/{type}")
    @Transactional(readOnly = true)
    @Override
    public List<ProductDto> getProductsByType(@PathVariable(name = "type") PRODUCT_TYPE productType) {
        List<ProductDto> listDto = new ArrayList<>();
        Iterable<ProductEntity> entityList = productDao.productsByType(productType);
        for (ProductEntity entity : entityList){
            ProductDto productDto = EntityToDto(entity);
            listDto.add(productDto);
        }
        return listDto;
    }

    // Update
    // http://localhost:8080/api/v1/product/update/id
    @PutMapping("/update/{id}")
    @CacheEvict(key = "#id", allEntries = true, cacheNames = { "product_list", "products" })
    @Override
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long id, ProductDto productDetails, MultipartFile imageFile) throws Throwable {

        ProductEntity productEntity = DtoToEntity(productDetails);
        ProductEntity product = (ProductEntity) productDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not exist with id --> " + id));

        String imageFileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));

        product.setProductName(productEntity.getProductName());
        product.setProductDescription(productEntity.getProductDescription());
        product.setProductType(productEntity.getProductType());
        product.setProductImageName(imageFileName);
        product.setProductPrice(productEntity.getProductPrice());

        ImageUpload.imageUpload(imageFileName,imageFile);

        ProductEntity updateProduct = (ProductEntity) productDao.save(product);
        ProductDto productDto = EntityToDto(updateProduct);

        return ResponseEntity.ok(productDto);
    }

    // Delete
    // http://localhost:8080/api/v1/product/delete/id
    @DeleteMapping("/delete/{id}")
    @CacheEvict(key = "#id", allEntries = true, cacheNames = { "product_list", "products" })
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Boolean deleteProduct(@PathVariable(name = "id" , required = true) Long id) throws Throwable {
        productDao.deleteById(id);
        return true;
    }

    @Override
    public ProductDto EntityToDto(ProductEntity productEntity) {
        ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
        return productDto;
    }

    @Override
    public ProductEntity DtoToEntity(ProductDto productDto) {
        ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
        return productEntity;
    }
}
