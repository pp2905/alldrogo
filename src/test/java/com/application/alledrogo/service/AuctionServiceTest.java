package com.application.alledrogo.service;

import com.application.alledrogo.model.Auction;
import com.application.alledrogo.model.Category;
import com.application.alledrogo.repository.AuctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionService auctionService;

    private Auction auction;
    private List<Auction> auctionList;

    private Category category;

    @BeforeEach
    void setUp() {
        auction = new Auction();
        auctionList = new ArrayList<>();
        category = new Category();

        auction.setCategory(category);
        auction.setTitle("Auto");
        auction.setOwnerId(1);
        auction.setDescription("Desc");
        auction.setPrice(BigDecimal.valueOf(10));
        auction.setQuantity(1);

        auctionList.add(auction);
    }

    @Test
    void shouldGetAllAuctions() {
        given(auctionRepository.findAll()).willReturn(auctionList);

        List<Auction> expectedAuctions = auctionService.getAllAuctions();

        assertThat(expectedAuctions).isNotEmpty();
        assertThat(expectedAuctions).hasSize(2);
        assertThat(expectedAuctions.get(0)).isEqualToComparingFieldByField(auction);
    }

    @Test
    void shouldGetAuctionsByCategoryId() {
        int id = category.getId();

        given(auctionRepository.findAllByCategoryId(id)).willReturn(auctionList);

        List<Auction> expectedAuctions = auctionService.getAuctionsByCategoryId(id);

        assertThat(expectedAuctions).isNotEmpty();
        assertThat(expectedAuctions).hasSize(1);
        assertThat(expectedAuctions).containsOnly(auction);
        assertThat(expectedAuctions).isEqualTo(auctionList);
    }

    @Test
    void getUserAuctions() {
        int ownerId = auction.getOwnerId();

        given(auctionRepository.findAllByOwnerId(ownerId)).willReturn(auctionList);

        List<Auction> expectedAuctions = auctionService.getUserAuctions(ownerId);

        assertThat(expectedAuctions).isNotEmpty();
        assertThat(expectedAuctions).hasSize(1);
        assertThat(expectedAuctions).containsOnly(auction);
        assertThat(expectedAuctions).isEqualTo(auctionList);
    }

    @Test
    void shouldGetAuctionById() {
        int id = auction.getId();

        given(auctionRepository.findById(id)).willReturn(java.util.Optional.ofNullable(auction));

        Auction expected = auctionService.getAuctionById(id);

        assertThat(expected).isNotNull();
        assertThat(expected).isEqualToComparingFieldByField(auction);
    }

    @Test
    void shouldAddAuction() {
       given(auctionRepository.findById(auction.getId())).willReturn(Optional.empty());
       given(auctionRepository.save(auction)).willReturn(auction);

       Auction expected = auctionService.addAuction(auction);

       assertThat(expected).isNotNull();
       assertThat(expected).isEqualToComparingFieldByField(auction);
    }

    @Test
    void shouldUpdateAuction() {
        int id = auction.getId();


        given(auctionRepository.findById(id)).willReturn(Optional.ofNullable(auction));
        given(auctionRepository.save(auction)).willReturn(auction);

        auction.setTitle("Car");
        Auction expected = auctionService.updateAuction(auction);

        assertThat(expected).isNotNull();
        assertThat(expected).isEqualToComparingFieldByField(auction);
    }

    @Test
    void shouldDeleteAuction() {
        int id = auction.getId();

        given(auctionRepository.findById(id)).willReturn(java.util.Optional.ofNullable(auction));
        auctionService.deleteAuction(id);

        verify(auctionRepository).delete(auction);
    }
}