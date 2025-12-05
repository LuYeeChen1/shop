package com.backend.shop.Controller.CartController;

import com.backend.shop.DataTransferObject.Cart.CartAddItemDTO;
import com.backend.shop.DataTransferObject.Cart.CartItemViewDTO;
import com.backend.shop.Model.Cart.CartItemModel;
import com.backend.shop.Model.Product.ProductModel;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Repository.Cart.CartRepository;
import com.backend.shop.Repository.Product.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository,
                          ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    /**
     * Show the cart page for the logged-in user.
     */
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {

        // Check login status
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        // Load cart items for this user
        List<CartItemModel> cartItems = cartRepository.findByUserId(loggedInUser.getUserId());

        // Map to view DTOs, including product name and price
        List<CartItemViewDTO> viewItems = cartItems.stream().map(item -> {
            CartItemViewDTO dto = new CartItemViewDTO();
            dto.setProductId(item.getProductId());
            dto.setQuantity(item.getQuantity());

            ProductModel product = productRepository.findById(item.getProductId());
            if (product != null) {
                dto.setProductName(product.getName());
                dto.setPrice(product.getPrice());
            }

            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("cartItems", viewItems);

        // Must match templates/cart/cart_view.html
        return "cart/cart_view";
    }

    /**
     * Handle add-to-cart requests from logged-in users.
     * Uses CartRepository for persistence.
     */
    @PostMapping("/cart/add")
    public String addToCart(
            HttpSession session,
            @ModelAttribute CartAddItemDTO cartAddItemDTO
    ) {
        // Check login status
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;

        Long productId = cartAddItemDTO.getProductId();
        Integer quantity = cartAddItemDTO.getQuantity();

        if (productId == null) {
            // Invalid request, redirect to product list
            return "redirect:/products";
        }

        if (quantity == null || quantity <= 0) {
            quantity = 1;
        }

        // Optional safety: check if product exists
        ProductModel product = productRepository.findById(productId);
        if (product == null) {
            // Product does not exist; redirect to product list
            return "redirect:/products";
        }

        Long userId = loggedInUser.getUserId();

        // Check if this product already exists in the user's cart
        CartItemModel existingItem = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingItem == null) {
            // No existing item: insert a new cart item
            CartItemModel newItem = new CartItemModel();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cartRepository.insert(newItem);
        } else {
            // Existing item: increase quantity
            int newQuantity = existingItem.getQuantity() + quantity;
            cartRepository.updateQuantity(userId, productId, newQuantity);
        }

        // After adding to cart, redirect to cart page
        return "redirect:/cart";
    }

    /**
     * Increase quantity of a cart item by 1.
     */
    @PostMapping("/cart/increase")
    public String increaseCartItem(
            HttpSession session,
            @RequestParam("productId") Long productId
    ) {
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;
        Long userId = loggedInUser.getUserId();

        if (productId == null) {
            return "redirect:/cart";
        }

        CartItemModel existingItem =
                cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + 1;
            cartRepository.updateQuantity(userId, productId, newQuantity);
        }

        return "redirect:/cart";
    }

    /**
     * Decrease quantity of a cart item by 1.
     * If quantity goes below 1, the item is removed.
     */
    @PostMapping("/cart/decrease")
    public String decreaseCartItem(
            HttpSession session,
            @RequestParam("productId") Long productId
    ) {
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;
        Long userId = loggedInUser.getUserId();

        if (productId == null) {
            return "redirect:/cart";
        }

        CartItemModel existingItem =
                cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() - 1;
            if (newQuantity < 1) {
                // If less than 1, delete the item
                cartRepository.deleteItem(userId, productId);
            } else {
                cartRepository.updateQuantity(userId, productId, newQuantity);
            }
        }

        return "redirect:/cart";
    }

    /**
     * Remove a single item from the user's cart.
     */
    @PostMapping("/cart/remove")
    public String removeFromCart(
            HttpSession session,
            @RequestParam("productId") Long productId
    ) {
        // Check login
        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;
        Long userId = loggedInUser.getUserId();

        if (productId != null) {
            cartRepository.deleteItem(userId, productId);
        }

        return "redirect:/cart";
    }

    /**
     * Clear all items from the user's cart.
     */
    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {

        Object sessionUser = session.getAttribute("loggedInUser");
        if (!(sessionUser instanceof AuthenticatedUser)) {
            return "redirect:/login";
        }

        AuthenticatedUser loggedInUser = (AuthenticatedUser) sessionUser;
        Long userId = loggedInUser.getUserId();

        cartRepository.clearCart(userId);

        return "redirect:/cart";
    }
}
