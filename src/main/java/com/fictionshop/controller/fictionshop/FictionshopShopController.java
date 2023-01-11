package com.fictionshop.controller.fictionshop;

import com.fictionshop.business.data.model.PRODUCT_TYPE;
import com.fictionshop.business.dto.ProductDto;
import com.fictionshop.business.services.ProductService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fictionshop")
@Scope("session")
public class FictionshopShopController {

    @Autowired
    private ProductService productService;

    @Getter
    @Setter
    private ShoppingCart shoppingCart;

    private ProductDto selectedProduct;
    private String discountCode = "";


    @GetMapping(value = "/shop")
    public String shopPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        selectedProduct = new ProductDto();
        return "pages/fictionshop/shop";
    }


    // Product Detail Starts ----

    @GetMapping("/product-details/{productId}")
    public String productDetails(@PathVariable("productId") Long productId, Model model) throws Throwable {
        selectedProduct = productService.getProductById(productId).getBody();
        assert selectedProduct != null;
        selectedProduct.setId(productId);
        model.addAttribute("selectedProduct", selectedProduct);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("shoppingCartItem", new ShoppingCartItem());
        return "pages/fictionshop/product-details";
    }

    // Product Detail Ends ----

    @PostConstruct
    private void init() {
        shoppingCart = new ShoppingCart();
        shoppingCart.setProductList(new ArrayList<ShoppingCartItem>());
    }

    // Discount check after a discountCode used
    public void discountCheck(){
        if(this.discountCode.equals("fiction50")){
            shoppingCart.setDiscount(shoppingCart.getCartItemPrice() * 0.5);
            shoppingCart.setTotalCartItemPrice(shoppingCart.getCartItemPrice() - shoppingCart.getDiscount());
        }else if(this.discountCode.equals("fiction25")){
            shoppingCart.setDiscount(shoppingCart.getCartItemPrice() * 0.25);
            shoppingCart.setTotalCartItemPrice(shoppingCart.getCartItemPrice() - shoppingCart.getDiscount());
        }
    }

    // Add product to Cart
    @PostMapping(value = "/shop")
    public String addToCart(@ModelAttribute("shoppingCartItem") ShoppingCartItem shoppingCartItem, Model model) throws Throwable {
        System.out.println(shoppingCartItem.getPiece());

        shoppingCartItem.setProductId(selectedProduct.getId());

        ProductDto productDto = productService.getProductById(shoppingCartItem.getProductId()).getBody();
        shoppingCartItem.setProduct(productDto);

        if(!shoppingCart.getProductList().isEmpty()) {
            for (int i = 0; i < shoppingCart.getProductList().size(); i++) {
                ShoppingCartItem newSCI = shoppingCart.getProductList().get(i);
                if (newSCI.getProductId() == shoppingCartItem.getProductId()) {

                    newSCI.setPiece(newSCI.getPiece() + shoppingCartItem.getPiece());
                    shoppingCart.getProductList().set(i, newSCI);
                    shoppingCart.setTotalCartItems(shoppingCart.getTotalCartItems() + shoppingCartItem.getPiece());

                    double newPrice = newSCI.getPiece() + (shoppingCartItem.getPiece() * newSCI.getProduct().getProductPrice());
                    shoppingCart.setCartItemPrice(shoppingCart.getCartItemPrice() + newPrice);
                    shoppingCart.setTotalCartItemPrice(shoppingCart.getCartItemPrice());

                    System.out.println(shoppingCart);
                    discountCheck();

                    return "redirect:/fictionshop/product-details/" + shoppingCartItem.getProductId();
                }
            }
        }

        shoppingCart.getProductList().add(shoppingCartItem);

        int productPiece = shoppingCart.getTotalCartItems();
        productPiece += shoppingCartItem.getPiece();

        double productPrice = shoppingCart.getCartItemPrice();
        productPrice += shoppingCartItem.getProduct().getProductPrice() * shoppingCartItem.getPiece();

        shoppingCart.setCartItemPrice(productPrice);
        shoppingCart.setTotalCartItemPrice(productPrice);
        shoppingCart.setTotalCartItems(productPiece);
        System.out.println(shoppingCart);

        discountCheck();

        model.addAttribute("products", productService.getAllProducts());
        selectedProduct = new ProductDto();

        return "redirect:/fictionshop/product-details/" + shoppingCartItem.getProductId();
    }

    //Shopping Cart Page
    @GetMapping(value = "/shop-cart")
    public String shopCart(Model model) {
        model.addAttribute("cartProducts", shoppingCart);
        model.addAttribute("cartItemPrice", shoppingCart.getCartItemPrice());
        model.addAttribute("totalCartItemPrice", shoppingCart.getTotalCartItemPrice());
        model.addAttribute("discountPrice", shoppingCart.getDiscount());
        return "pages/fictionshop/shop-cart";
    }

    // Remove From ShoppingCartItems
    @GetMapping(value = "/shop-cart/remove/{shopCartItemId}")
    public String removeShopCartItem(@PathVariable Long shopCartItemId){
        ShoppingCartItem sci = shoppingCart.getProductList().stream().filter(shoppingCartItem -> shoppingCartItem.getShoppingCartItemId().equals(shopCartItemId)).findFirst().get();

        Integer sciPiece = sci.getPiece();
        shoppingCart.setTotalCartItems(shoppingCart.getTotalCartItems() - sciPiece);
        Double sciPrice = sci.getProduct().getProductPrice() * sciPiece;
        shoppingCart.setCartItemPrice(shoppingCart.getCartItemPrice() - sciPrice);

        shoppingCart.getProductList().remove(sci);

        discountCheck();

        return "redirect:/fictionshop/shop-cart";
    }

    // Discount with discountCode
    @PostMapping(value = "/shop-cart/discount")
    public String discountButton(@RequestParam("discountCode") String code, Model model){
        this.discountCode = code;
        Double total = shoppingCart.getCartItemPrice();
        Double discount = shoppingCart.getDiscount();
        System.out.println(code);

        if(code.equals("fiction50")){
            discount = total * 0.5;
            total -= (discount);
        }else if(code.equals("fiction25")){
            discount = total * 0.25;
            total -= (discount);
        }else{
            System.out.println("Wrong Code");
        }

        shoppingCart.setDiscount(discount);
        shoppingCart.setTotalCartItemPrice(total);
        return "redirect:/fictionshop/shop-cart";
    }

    @PostMapping(value = "/shop-cart/updateCart")
    public String updateCart(@RequestParam("quantityChange") Integer newQuantity){

        //Tamamlanacak..


        return "redirect:/fictionshop/shop-cart";
    }


}


@Data
@ToString
class ShoppingCartItem{

    ShoppingCartItem(){
        setShoppingCartItemId(System.currentTimeMillis());
    }

    private Long shoppingCartItemId;
    private Long productId;
    private Integer piece;
    private ProductDto product;
}

@Data
@ToString
class ShoppingCart{
    private Integer totalCartItems=0;
    private Double discount = 0.0;
    private Double CartItemPrice=0.0;
    private Double totalCartItemPrice=0.0;

    private List<ShoppingCartItem> productList = new ArrayList<>();
}
