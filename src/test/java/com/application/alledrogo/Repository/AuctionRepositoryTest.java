package com.application.alledrogo.Repository;

import com.application.alledrogo.Model.Auction;
import com.application.alledrogo.Model.Category;
import com.application.alledrogo.Model.SubCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AuctionRepositoryTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

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

    @Test
    void findAllBySubCategoryId() {
        auction = new Auction();
        auction2 = new Auction();

        SubCategory subCategory = new SubCategory();
        subCategory.setName("first");

        SubCategory subCategory2 = new SubCategory();
        subCategory2.setName("second");

        SubCategory getSubCategory = subCategoryRepository.save(subCategory);
        SubCategory getSubCategory2 = subCategoryRepository.save(subCategory2);

        auction.setSubCategory(getSubCategory);
        auction2.setSubCategory(getSubCategory2);

        List<Auction> auctions = Arrays.asList(auction, auction2);
        auctionRepository.saveAll(auctions);

        List<Auction> foundAuctions = auctionRepository.findAllBySubCategoryId(subCategory.getId());
        assertThat(foundAuctions).containsOnly(auction);
    }
}