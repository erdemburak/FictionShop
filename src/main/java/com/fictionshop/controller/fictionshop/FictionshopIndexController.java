package com.fictionshop.controller.fictionshop;

import com.fictionshop.business.data.model.PRODUCT_TYPE;
import com.fictionshop.business.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fictionshop")
public class FictionshopIndexController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = {"/","/index"})
    public String fictionshopDashboard(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "pages/fictionshop/index";
    }

    @GetMapping(value = "/contact")
    public String fictionshopContact(){
        return "pages/fictionshop/contact";
    }


    @GetMapping(value = "/type/{productType}")
    public String pageByType(@PathVariable("productType") PRODUCT_TYPE productType, Model model) {
        model.addAttribute("products", productService.getProductsByType(productType));

        return "pages/fictionshop/shop";
    }


}
