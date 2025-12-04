package com.backend.shop.Controller.ProductController;

import com.backend.shop.DataTransferObject.Product.ProductDetailDTO;
import com.backend.shop.Model.Product.ProductModel;
import com.backend.shop.Repository.Product.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductPublicController {

    private final ProductRepository productRepository;

    public ProductPublicController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Show product detail for customers / visitors.
     */
    @GetMapping("/products/detail")
    public String showProductDetail(
            @RequestParam("productId") Long productId,
            Model model
    ) {
        // Load product from DB
        ProductModel product = productRepository.findById(productId);
        if (product == null) {
            // If not found, redirect to home or product list
            return "redirect:/";
        }

        // Map ProductModel -> ProductDetailDTO
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());
        dto.setCreatedAt(product.getCreatedAt());

        model.addAttribute("product", dto);

        // Must match templates/product/product_detail.html
        return "product/product_detail";
    }
}
