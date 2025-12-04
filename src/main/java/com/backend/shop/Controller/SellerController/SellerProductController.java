package com.backend.shop.Controller.SellerController;

import com.backend.shop.DataTransferObject.Product.ProductCreateDTO;
import com.backend.shop.DataTransferObject.Product.ProductEditDTO;
import com.backend.shop.DataTransferObject.Product.ProductListItemDTO;
import com.backend.shop.DataTransferObject.Product.SellerProductsDTO;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

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
     * Show list of products for current seller.
     */
    @GetMapping("/products")
    public String showSellerProducts(HttpSession session, Model model) {

        // Check login
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

        // Load products from repository
        List<ProductModel> productModels =
                productRepository.findBySellerUserId(loggedInUser.getUserId());

        // Map ProductModel -> ProductListItemDTO
        List<ProductListItemDTO> productDTOs = productModels.stream().map(p -> {
            ProductListItemDTO dto = new ProductListItemDTO();
            dto.setProductId(p.getProductId());
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            dto.setStockQuantity(p.getStockQuantity());
            dto.setCategory(p.getCategory());
            dto.setCreatedAt(p.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        // Build page DTO
        SellerProductsDTO pageDTO = new SellerProductsDTO();
        pageDTO.setSellerUserId(loggedInUser.getUserId());
        pageDTO.setSellerUsername(loggedInUser.getUsername());
        pageDTO.setSellerEmail(loggedInUser.getEmail());
        pageDTO.setProducts(productDTOs);

        model.addAttribute("sellerProducts", pageDTO);

        // Must match templates/seller/product_list.html
        return "seller/product_list";
    }

    /**
     * Show form for creating a new product.
     */
    @GetMapping("/products/new")
    public String showCreateProductForm(HttpSession session, Model model) {

        // Check login
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

        // Check login
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
        product.setSellerUserId(loggedInUser.getUserId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());

        // Save to DB via repository (no service)
        productRepository.save(product);

        // After creation, redirect to product list
        return "redirect:/seller/products";
    }

    /**
     * Show form for editing an existing product.
     */
    @GetMapping("/products/edit")
    public String showEditProductForm(
            HttpSession session,
            @RequestParam("productId") Long productId,
            Model model
    ) {
        // Check login
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

        // Load the product
        ProductModel product = productRepository.findById(productId);
        if (product == null) {
            // Product not found
            return "redirect:/seller/products";
        }

        // Ensure the product belongs to this seller
        if (!loggedInUser.getUserId().equals(product.getSellerUserId())) {
            // Not allowed to edit others' products
            return "redirect:/seller/products";
        }

        // Build DTO from product
        ProductEditDTO dto = new ProductEditDTO();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());

        model.addAttribute("productEditDTO", dto);
        model.addAttribute("productId", productId);

        // Must match templates/seller/edit_product.html
        return "seller/edit_product";
    }

    /**
     * Handle product edit submission.
     */
    @PostMapping("/products/edit")
    public String handleEditProduct(
            HttpSession session,
            @RequestParam("productId") Long productId,
            @Valid @ModelAttribute("productEditDTO") ProductEditDTO dto,
            BindingResult bindingResult,
            Model model
    ) {

        // Check login
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

        // Load existing product to check ownership
        ProductModel existing = productRepository.findById(productId);
        if (existing == null) {
            return "redirect:/seller/products";
        }

        if (!loggedInUser.getUserId().equals(existing.getSellerUserId())) {
            // Not allowed to edit others' products
            return "redirect:/seller/products";
        }

        if (bindingResult.hasErrors()) {
            // Validation failed, show form again
            model.addAttribute("productId", productId);
            return "seller/edit_product";
        }

        // Build updated product model
        ProductModel toUpdate = new ProductModel();
        toUpdate.setProductId(productId);
        toUpdate.setName(dto.getName());
        toUpdate.setDescription(dto.getDescription());
        toUpdate.setPrice(dto.getPrice());
        toUpdate.setStockQuantity(dto.getStockQuantity());
        toUpdate.setCategory(dto.getCategory());
        toUpdate.setImageUrl(dto.getImageUrl());

        // Repository update
        productRepository.update(toUpdate);

        // After update, redirect to product list
        return "redirect:/seller/products";
    }

    /**
     * Show confirm page before deleting a product.
     */
    @GetMapping("/products/delete-confirm")
    public String showDeleteConfirmPage(
            HttpSession session,
            @RequestParam("productId") Long productId,
            Model model
    ) {
        // Check login
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

        // Load the product
        ProductModel product = productRepository.findById(productId);
        if (product == null) {
            // Product not found
            return "redirect:/seller/products";
        }

        // Ensure the product belongs to this seller
        if (!loggedInUser.getUserId().equals(product.getSellerUserId())) {
            // Not allowed to delete others' products
            return "redirect:/seller/products";
        }

        // Put product into model to show details on page
        model.addAttribute("product", product);
        model.addAttribute("sellerUsername", loggedInUser.getUsername());
        model.addAttribute("sellerEmail", loggedInUser.getEmail());

        // Must match templates/seller/confirm_delete_product.html
        return "seller/confirm_delete_product";
    }

    /**
     * Handle product delete.
     */
    @PostMapping("/products/delete")
    public String handleDeleteProduct(
            HttpSession session,
            @RequestParam("productId") Long productId
    ) {

        // Check login session
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Check if user is an approved seller
        SellerModel seller = sellerRepository.findByUserId(loggedInUser.getUserId());
        if (seller == null || seller.getStatus() != SellerStatus.APPROVED) {
            return "redirect:/customer/dashboard";
        }

        // Find product
        ProductModel existingProduct = productRepository.findById(productId);
        if (existingProduct == null) {
            return "redirect:/seller/products";
        }

        // Ensure seller owns this product
        if (!loggedInUser.getUserId().equals(existingProduct.getSellerUserId())) {
            return "redirect:/seller/products";
        }

        // Delete the product
        productRepository.deleteProductById(productId);

        // Redirect back to product list
        return "redirect:/seller/products";
    }

}
