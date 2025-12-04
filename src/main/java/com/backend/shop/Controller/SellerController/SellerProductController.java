package com.backend.shop.Controller.SellerController;

import com.backend.shop.DataTransferObject.Product.ProductCreateDTO;
import com.backend.shop.Model.Product.ProductModel;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Repository.Product.ProductRepository;
import com.backend.shop.Repository.Seller.SellerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerProductController {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public SellerProductController(ProductRepository productRepository,
                                   SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    /**
     * Show form for creating a new product.
     */
    @GetMapping("/products/new")
    public String showCreateProductForm(HttpSession session, Model model) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Ensure this user is an APPROVED seller
        SellerModel seller = sellerRepository.findByUserId(loggedInUser.getUserId());
        if (seller == null || seller.getStatus() != SellerStatus.APPROVED) {
            return "redirect:/customer/dashboard";
        }

        ProductCreateDTO dto = new ProductCreateDTO();
        model.addAttribute("productCreateDTO", dto);

        // Must match templates/seller/create_product.html
        return "seller/create_product";
    }

    /**
     * Handle product creation submission.
     */
    @PostMapping("/products/new")
    public String handleCreateProduct(
            HttpSession session,
            @Valid @ModelAttribute("productCreateDTO") ProductCreateDTO dto,
            BindingResult bindingResult,
            Model model
    ) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Ensure seller is approved
        SellerModel seller = sellerRepository.findByUserId(loggedInUser.getUserId());
        if (seller == null || seller.getStatus() != SellerStatus.APPROVED) {
            return "redirect:/customer/dashboard";
        }

        if (bindingResult.hasErrors()) {
            // Validation failed, show form again
            return "seller/create_product";
        }

        // Build ProductModel from DTO
        ProductModel product = new ProductModel();
        product.setSellerUserId(loggedInUser.getUserId()); // 假设你有这个字段
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());

        // Save to DB via repository (no service)
        productRepository.save(product);

        // After creation, redirect to seller dashboard (or product list in future)
        return "redirect:/seller/dashboard";
    }
}
