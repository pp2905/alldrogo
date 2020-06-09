package com.application.alledrogo.repository;

import com.application.alledrogo.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    List<Auction> findAllByOwnerId(int ownerId);
    List<Auction> findAllByCategoryId(int categoryId);
}
