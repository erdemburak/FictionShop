package com.fictionshop.business.dto;

import com.fictionshop.business.data.model.PRODUCT_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class ProductDto {
    private Long id;
    private String productName;
    private String productDescription;
    private PRODUCT_TYPE productType;
    private Double productPrice;
    private String productImageName;
}
