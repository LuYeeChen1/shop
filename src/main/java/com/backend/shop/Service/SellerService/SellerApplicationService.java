package com.backend.shop.Service.SellerService;

import com.backend.shop.DataTransferObject.Seller.SellerApplicationDTO;
import com.backend.shop.DataTransferObject.Seller.SellerProfileUpdateDTO;
import com.backend.shop.Model.Seller.SellerModel;
import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.Seller.SellerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SellerApplicationService {

    private final SellerRepository sellerRepository;

    public SellerApplicationService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Handle a new seller application from a customer.
     */
    public void applyForSeller(Long userId, SellerApplicationDTO dto) {

        // If seller record already exists, treat this as a re-application
        SellerModel existing = sellerRepository.findByUserId(userId);

        if (existing == null) {
            // Create new seller record
            SellerModel seller = new SellerModel();
            seller.setUserId(userId);
            seller.setShopName(dto.getShopName());
            seller.setShopDescription(dto.getShopDescription());
            seller.setShopLogoUrl(dto.getShopLogoUrl());
            seller.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
            seller.setBusinessAddress(dto.getBusinessAddress());
            seller.setContactNumber(dto.getContactNumber());
            seller.setStatus(SellerStatus.PENDING);
            seller.setAppliedAt(LocalDateTime.now());

            sellerRepository.save(seller);

        } else {
            // Update fields and set status back to PENDING
            existing.setShopName(dto.getShopName());
            existing.setShopDescription(dto.getShopDescription());
            existing.setShopLogoUrl(dto.getShopLogoUrl());
            existing.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
            existing.setBusinessAddress(dto.getBusinessAddress());
            existing.setContactNumber(dto.getContactNumber());
            existing.setStatus(SellerStatus.PENDING);
            existing.setAppliedAt(LocalDateTime.now());
            existing.setReviewComment(null);
            existing.setReviewedAt(null);
            existing.setReviewedByAdmin(null);

            sellerRepository.updateProfile(existing);
            // You may also want a separate method to update status/review fields,
            // but for simplicity we only reuse updateProfile here.
            sellerRepository.updateStatus(
                    userId,
                    SellerStatus.PENDING,
                    null,
                    null
            );
        }
    }

    /**
     * Update basic seller profile for an approved seller.
     */
    public void updateSellerProfile(Long userId, SellerProfileUpdateDTO dto) {

        SellerModel seller = sellerRepository.findByUserId(userId);
        if (seller == null) {
            // In a real app you might throw an exception or handle this case explicitly.
            return;
        }

        seller.setShopName(dto.getShopName());
        seller.setShopDescription(dto.getShopDescription());
        seller.setShopLogoUrl(dto.getShopLogoUrl());
        seller.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        seller.setBusinessAddress(dto.getBusinessAddress());
        seller.setContactNumber(dto.getContactNumber());

        sellerRepository.updateProfile(seller);
    }
}
