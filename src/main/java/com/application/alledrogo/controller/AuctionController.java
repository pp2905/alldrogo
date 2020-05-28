package com.application.alledrogo.controller;

import com.application.alledrogo.model.Auction;
import com.application.alledrogo.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping(
            path = "/user/{ownerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Auction> getAuctionsByOwnerId(@PathVariable("ownerId") int id) {
        return auctionService.getUserAuctions(id);
    }

    @GetMapping(
            path = "/active",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Auction> getActiveAuctions(
            @RequestParam("categoryId") Optional<Integer> categoryId,
            @RequestParam("subCategoryId") Optional<Integer> subCategoryId,
            @RequestParam("ownerId") Optional<Integer> id,
            @RequestParam("title") Optional<String> title,
            @RequestParam("priceFrom") Optional<Double> priceFrom,
            @RequestParam("priceTo") Optional<Double> priceTo
    ) {
        LocalDateTime now = LocalDateTime.now();
        return auctionService.getFilteredAuction(categoryId, subCategoryId, id, title, priceFrom, priceTo, Optional.of(now), Optional.empty());
    }

    @GetMapping(
            path = "/ended",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Auction> getEndedAuctions(
            @RequestParam("categoryId") Optional<Integer> categoryId,
            @RequestParam("subCategoryId") Optional<Integer> subCategoryId,
            @RequestParam("ownerId") Optional<Integer> id,
            @RequestParam("title") Optional<String> title,
            @RequestParam("priceFrom") Optional<Double> priceFrom,
            @RequestParam("priceTo") Optional<Double> priceTo
    ) {
        LocalDateTime now = LocalDateTime.now();
        return auctionService.getFilteredAuction(categoryId, subCategoryId, id, title, priceFrom, priceTo, Optional.empty(), Optional.of(now));
    }

    @GetMapping(
            path = "{auctionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Auction getAuctionById(@PathVariable("auctionId") int id) {
        return auctionService.getAuctionById(id);
    }

    @GetMapping(
            path = "/exchangeRate/{currency}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Double getExchangeRate(@PathVariable String currency) {
        return auctionService.getExchangeCourse(currency);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Auction addAuction(@RequestBody Auction auction) {
        return auctionService.addAuction(auction);
    }

    @PostMapping(
            path = "buy/{auctionId}"
    )
    public void buy(@PathVariable("auctionId") int id) {
        auctionService.buy(id);
    }

    @PostMapping(
            path = "addAuctionAgain/{auctionId}"
    )
    public void addAuctionAgain(@PathVariable("auctionId") int id, @RequestParam Optional<Integer> numberOfDays) {
        auctionService.addAuctionAgain(id, numberOfDays);
    }

    @PostMapping(
            path = "/extendAuction/{auctionId}"
    )
    public void extendAuction(@PathVariable("auctionId") int id, @RequestParam Optional<Integer> numberOfDays) {
        auctionService.extendAuction(id, numberOfDays);
    }

    @PostMapping(
            path = "/endAuction/{auctionId}"
    )
    public void endAuction(@PathVariable("auctionId") int id) {
        auctionService.endAuction(id);
    }

    @PutMapping(
            path = "{auctionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Auction editAuctionById(@PathVariable("auctionId") int id, @RequestBody Auction auction) {
        auction.setId(id);
        return auctionService.updateAuction(auction);
    }

    @DeleteMapping(
            path = "{auctionId}"
    )
    public void deleteAuction(@PathVariable("auctionId") int id) {
        auctionService.deleteAuction(id);
    }
}
