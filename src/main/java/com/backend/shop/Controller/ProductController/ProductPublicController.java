package com.backend.shop.Controller.ProductController;

import com.backend.shop.DataTransferObject.Product.ProductDetailDTO;
import com.backend.shop.DataTransferObject.Product.ProductListItemDTO;
import com.backend.shop.Model.Product.ProductModel;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Repository.Product.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductPublicController {

    private final ProductRepository productRepository;

    public ProductPublicController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Show public product list for customers/visitors.
     */
    @GetMapping("/products")
    public String showPublicProductList(Model model) {

        // Load all products from DB
        List<ProductModel> productModels = productRepository.findAll();

        // Convert ProductModel -> ProductListItemDTO
        List<ProductListItemDTO> products = productModels.stream().map(p -> {
            ProductListItemDTO dto = new ProductListItemDTO();
            dto.setProductId(p.getProductId());
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            dto.setStockQuantity(p.getStockQuantity());
            dto.setCategory(p.getCategory());
            dto.setCreatedAt(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("products", products);

        // Must match templates/product/product_list.html
        return "product/product_list";
    }

    /**
     * Show product detail for customers / visitors.
     */
    @GetMapping("/products/detail")
    public String showProductDetail(
            @RequestParam("productId") Long productId,
            HttpSession session,
            Model model
    ) {
        // Load product from DB
        ProductModel product = productRepository.findById(productId);
        if (product == null) {
            return "redirect:/products"; // fallback to list
        }

        // Convert ProductModel -> ProductDetailDTO
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

        // Add loggedInUser info to model (guest vs logged-in)
        Object sessionUser = session.getAttribute("loggedInUser");
        if (sessionUser instanceof AuthenticatedUser) {
            model.addAttribute("loggedInUser", (AuthenticatedUser) sessionUser);
        } else {
            model.addAttribute("loggedInUser", null);
        }

        // Must match templates/product/product_detail.html
        return "product/product_detail";
    }
}
