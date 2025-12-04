package com.backend.shop.Service.SellerService;

import com.backend.shop.Model.Seller.SellerStatus;
import com.backend.shop.Repository.Seller.SellerRepository;
import org.springframework.stereotype.Service;

@Service
public class SellerAdminService {

    private final SellerRepository sellerRepository;

    public SellerAdminService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Approve a seller application.
     */
    public void approveSeller(Long userId, String adminEmail, String comment) {
        if (comment == null || comment.isBlank()) {
            comment = "Approved";
        }
        sellerRepository.updateStatus(
                userId,
                SellerStatus.APPROVED,
                adminEmail,
                comment
        );
    }

    /**
     * Reject a seller application.
     */
    public void rejectSeller(Long userId, String adminEmail, String comment) {
        if (comment == null || comment.isBlank()) {
            comment = "Rejected by admin";
        }
        sellerRepository.updateStatus(
                userId,
                SellerStatus.REJECTED,
                adminEmail,
                comment
        );
    }

    /**
     * Deactivate an existing seller account.
     */
    public void deactivateSeller(Long userId, String adminEmail) {
        sellerRepository.updateStatus(
                userId,
                SellerStatus.DEACTIVATED,
                adminEmail,
                "Seller account deactivated."
        );
    }
}
