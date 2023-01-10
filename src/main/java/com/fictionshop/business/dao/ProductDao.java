package com.fictionshop.business.dao;

import com.fictionshop.business.data.entity.ProductEntity;
import com.fictionshop.business.data.model.PRODUCT_TYPE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<ProductEntity, Long> {

    @Query("Select p FROM ProductEntity p WHERE p.productType = :productType")
    public List<ProductEntity> productsByType(@Param("productType") PRODUCT_TYPE productType);

}
