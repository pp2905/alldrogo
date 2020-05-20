package com.application.alledrogo.Repository;

import com.application.alledrogo.Model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    List<Auction> findAllByOwnerId(int ownerId);
    List<Auction> findAllByCategoryId(int categoryId);
    List<Auction> findAllBySubCategoryId(int subCategoryId);
}
