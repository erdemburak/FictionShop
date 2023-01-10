package com.fictionshop.business.data.entity;

import com.fictionshop.business.data.model.PRODUCT_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productentity")
@Data
@Builder
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE ProductEntity SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ProductEntity extends BaseEntity implements Serializable {

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PRODUCT_TYPE productType;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(name = "product_image_name")
    private String productImageName;

}
