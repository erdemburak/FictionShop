package com.fictionshop.controller.admin;

import com.fictionshop.business.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Scope("session")
public class AdminIndexController {

    @Autowired
    ProductService productService;

    // http://localhost:8080/admin/index.html
    @GetMapping(value = {"/","/index"})
    public String indexPage(Model model){
        model.addAttribute("productList", productService.getAllProducts());
        return "pages/admin/index";
    }
}
