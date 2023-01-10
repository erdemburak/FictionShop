package com.fictionshop.controller.admin;

import com.fictionshop.business.dto.ProductDto;
import com.fictionshop.business.services.ProductService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@Data
@Log4j2
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    ProductService productService;

    private ProductDto selectedProduct;
    private Long deleteProductId = 0L;
    private Long selectedProductId = 0L;
    private ProductDto updatedProduct = new ProductDto();

    @GetMapping(value = {"/"})
    public String productListPage(Model model){
        model.addAttribute("productList", productService.getAllProducts());
        return "pages/admin/products";
    }

    // Create Start ----
    @GetMapping(value = "/create")
    public String createProductPage(Model model){
        model.addAttribute("newProduct", new ProductDto());
        return "pages/admin/create-product";
    }

    @PostMapping(value = "/")
    public String postProduct(@Valid @ModelAttribute("newProduct") ProductDto productDto, BindingResult bindingResult, Model model,@RequestParam(value = "imageUpload") MultipartFile file) throws IOException {
        if(bindingResult.hasErrors()){
            log.error("Hata var....");
            System.out.println("Hata var....");
            return "pages/admin/create-product";
        }
        log.info("Success !", productDto);

        productService.createProduct(productDto,file);



        model.addAttribute("productList", productService.getAllProducts());
        System.out.println(productDto);
        return "redirect:/admin/product/";
    }
    // Create End -----

    // Update Start ----

    @GetMapping(value = "/update-select")
    public String updateGetProductsPage(Model model){
        model.addAttribute("selectProduct" , new ProductDto());
        model.addAttribute("productList", productService.getAllProducts());
        return "pages/admin/select-update-product";
    }

    @PostMapping (value = "/update-select")
    public String updateProductdenemePage(@Valid @RequestBody @ModelAttribute("selectProduct") ProductDto productDto, BindingResult bindingResult, Model model) throws Throwable {
        if(bindingResult.hasErrors()){
            log.error("Hata var....");
            System.out.println("Hata var....");
            return "pages/admin/select-update-product";
        }
        log.info("Success !", productDto);
        this.selectedProductId = productDto.getId();
        this.setSelectedProduct(productService.getProductById(this.selectedProductId).getBody());
        System.out.println(this.getSelectedProduct());

        model.addAttribute("selectedProduct" , this.getSelectedProduct());
        model.addAttribute("updatedProduct" , this.getSelectedProduct());
        model.addAttribute("productList", productService.getAllProducts());
        return "pages/admin/update-selected-product";
    }

    @GetMapping(value = "/update-selected")
    public String selectedProduct(Model model){
        return "/pages/admin/update-selected-product";
    }

    @PostMapping (value = "/update-selected")
    public String updateProductPage(@Valid @RequestBody @ModelAttribute("updatedProduct") ProductDto productDto, BindingResult bindingResult, Model model, @RequestParam(value = "imageUpload") MultipartFile file) throws Throwable {
        if(bindingResult.hasErrors()){
            log.error("Hata var....");
            System.out.println("Hata var....");
            return "pages/admin/update-selected-product";
        }
        productDto.setId(this.selectedProduct.getId());
        log.info("Success !", productDto);
        System.out.println(productDto.getId());
        System.out.println(productDto);
        productService.updateProduct(productDto.getId(), productDto, file);
        model.addAttribute("productList", productService.getAllProducts());
        return "redirect:/admin/product/";
    }
    // Update End ----

    // Delete Start ----

    @GetMapping(value = "/delete-select")
    public String deleteSelectedProduct(Model model){
        model.addAttribute("deleteProduct" , new ProductDto());
        model.addAttribute("productList", productService.getAllProducts());
        return "pages/admin/delete-selected-product";
    }


    @PostMapping (value = "/delete-select")
    public String deleteProductPage(@Valid @ModelAttribute("deleteProduct") ProductDto productDto, BindingResult bindingResult, Model model) throws Throwable {
        if(bindingResult.hasErrors()){
            log.error("Hata var....");
            System.out.println("Hata var....");
            return "pages/admin/delete-selected-product";
        }
        log.info("Success !", productDto);
        this.deleteProductId = productDto.getId();
        productService.deleteProduct(this.deleteProductId);
        model.addAttribute("productList", productService.getAllProducts());
        return "redirect:/admin/product/delete-select";
    }
    // Delete End ----



}
