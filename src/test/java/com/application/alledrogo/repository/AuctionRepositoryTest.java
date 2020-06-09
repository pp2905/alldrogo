package com.application.alledrogo.repository;

import com.application.alledrogo.model.Auction;
import com.application.alledrogo.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AuctionRepositoryTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    Auction auction;
    Auction auction2;

    @Test
    void findAllByOwnerId() {
        auction = new Auction();
        auction2 = new Auction();

        auction.setOwnerId(1);
        auction2.setOwnerId(2);

        List<Auction> auctions = Arrays.asList(auction, auction2);
        auctionRepository.saveAll(auctions);

        List<Auction> found = auctionRepository.findAllByOwnerId(auction.getOwnerId());
        assertThat(found).containsOnly(auction);
    }

    @Test
    void findAllByCategoryId() {
        auction = new Auction();
        auction2 = new Auction();

        Category category = new Category();
        category.setName("first");

        Category category2 = new Category();
        category2.setName("second");

        Category getCategory = categoryRepository.save(category);
        Category getCategory2 = categoryRepository.save(category2);

        auction.setCategory(getCategory);
        auction2.setCategory(getCategory2);

        List<Auction> auctions = Arrays.asList(auction, auction2);
        auctionRepository.saveAll(auctions);

        List<Auction> foundAuctions = auctionRepository.findAllByCategoryId(getCategory.getId());
        assertThat(foundAuctions).containsOnly(auction);
    }
}