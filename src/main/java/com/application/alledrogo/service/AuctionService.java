package com.application.alledrogo.service;

import com.application.alledrogo.exception.NotAcceptableException;
import com.application.alledrogo.exception.NotFoundException;
import com.application.alledrogo.model.Auction;
import com.application.alledrogo.repository.AuctionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllAuctions() {
        List<Auction> getAuctions = auctionRepository.findAll();
        if(getAuctions.isEmpty()) {
            throw new NotFoundException("Not found any Auctions");
        }

        return getAuctions;
    }

    public List<Auction> getAuctionsByCategoryId(int categoryId) {
        List<Auction> getAuctions = auctionRepository.findAllByCategoryId(categoryId);
        if(getAuctions.isEmpty()) {
            throw new NotFoundException(String.format("Not found any Auctions with categoryId: %s", categoryId));
        }

        return getAuctions;
    }

    public List<Auction> getUserAuctions(int ownerId) {
        List<Auction> getAuctions = auctionRepository.findAllByOwnerId(ownerId);

        return getAuctions;
    }

    public List<Auction> getFilteredAuction(Optional<Integer> categoryId, Optional<Integer> ownerId, Optional<String> title, Optional<BigDecimal> priceFrom, Optional<BigDecimal> priceTo, Optional<LocalDateTime> endDateFrom, Optional<LocalDateTime> endDateTo) {
        //getAuctions check if the any auction exist in the database, if not throw NotFoundException (404 not found)
        List<Auction> filteredAuctions = getAllAuctions();

        if(categoryId.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getCategory().getId() == categoryId.get()).collect(Collectors.toList());
        }

        if(ownerId.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getOwnerId() == ownerId.get()).collect(Collectors.toList());
        }

        if(title.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getTitle().contains(title.get())).collect(Collectors.toList());
        }

        if(priceFrom.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getPrice().compareTo(priceFrom.get()) >= 0 ).collect(Collectors.toList());
        }

        if(priceTo.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getPrice().compareTo(priceTo.get()) <= 0 ).collect(Collectors.toList());
        }

        if(endDateFrom.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> (auction.getEndDate().compareTo(endDateFrom.get())) >= 0).collect(Collectors.toList());
        }

        if(endDateTo.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> (auction.getEndDate().compareTo(endDateTo.get())) <= 0).collect(Collectors.toList());
        }

        return filteredAuctions;
    }

    public Auction getAuctionById(int id) {
        Optional<Auction> getAuction = auctionRepository.findById(id);
        return getAuction.orElseThrow(() -> new NotFoundException(String.format("Not found Category with id %s", id)));
    }

    @SneakyThrows
    public Double getExchangeCourse(String currency) {
        Double exchangeRate = 0.0;

        if(!currency.equals("eur") && !currency.equals("usd")) {

        } else if (currency.equals("eur") || currency.equals("usd")) {
            final String url = "http://api.nbp.pl/api/exchangerates/rates/a/"+currency;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode rates = root.path("rates");

            exchangeRate = rates.get(0).path("mid").asDouble();
        }

        return exchangeRate;
    }

    public Auction addAuction(Auction auction) {
        if(auction.getCategory() == null || auction.getOwnerId() == 0 || auction.getTitle() == null || auction.getDescription() == null ||  auction.getPrice().equals(0) || auction.getQuantity() == 0) {
            throw new NotAcceptableException("Category, OwnerId, Title, Description, Price, Quantity should not be empty");
        }

        LocalDateTime now = LocalDateTime.now();
        auction.setStartDate(now);
        auction.setBaseQuantity(auction.getQuantity());

        if(auction.getEndDate() == null || (auction.getEndDate().compareTo(now) < 0)) {
            auction.setEndDate(now.plusDays(14));
        }

        return auctionRepository.save(auction);
    }

    public Auction updateAuction(Auction auction) {
        //getAuctionById check if the Auction exist in the database, if not throw NotFoundException (404 not found)
        Auction getAuction = getAuctionById(auction.getId());

        return auctionRepository.save(auction);
    }

    public void addAuctionAgain(int id, Optional<Integer> numberOfDays) {
        //getAuctionById check if the Auction exist in the database, if not throw NotFoundException (404 not found)
        Auction getAuction = getAuctionById(id);

        if(isAuctionActive(getAuction)) {
            throw new NotAcceptableException(String.format("Auction with id: %s is already active", id));
        } else {
            int daysToAdd = 14;

            if(numberOfDays.isPresent()) {
                daysToAdd = numberOfDays.get();
            }

            LocalDateTime now = LocalDateTime.now();
            getAuction.setEndDate(now.plusDays(daysToAdd));

            auctionRepository.save(getAuction);
        }
    }

    public void buy(int auctionId) {
        //getAuctionById check if the Auction exist in the database, if not throw NotFoundException (404 not found)
        Auction getAuction = getAuctionById(auctionId);

        if(!isAuctionActive(getAuction)) {
            throw new NotAcceptableException(String.format("Auction with id: %s is already ended", auctionId));
        }

        int auctionQuantity = getAuction.getQuantity();

        if(auctionQuantity == 1) {
            getAuction.setEndDate(LocalDateTime.now());
            getAuction.setQuantity(0);
        } else {
            getAuction.setQuantity(auctionQuantity - 1);
        }

        auctionRepository.save(getAuction);
    }

    public void extendAuction(int id, Optional<Integer> numberOfDays) {
        //getAuctionById check if the Auction exist in the database, if not throw NotFoundException (404 not found)
        Auction getAuction = getAuctionById(id);

        if(!isAuctionActive(getAuction)) {
            throw new NotAcceptableException(String.format("Auction with id: %s is already ended", id));
        } else {
            int daysToAdd = 14;

            if(numberOfDays.isPresent()) {
                daysToAdd = numberOfDays.get();
            }

            LocalDateTime actualEndDate = getAuction.getEndDate();
            getAuction.setEndDate(actualEndDate.plusDays(daysToAdd));
            auctionRepository.save(getAuction);
        }
    }

    public void endAuction(int id) {
        //getAuctionById check if the Auction exist in the database, if not throw NotFoundException (404 not found)
        Auction getAuction = getAuctionById(id);

        if(!isAuctionActive(getAuction)) {
            throw new NotAcceptableException(String.format("Auction with id: %s is already ended", id));
        } else {
            getAuction.setEndDate(LocalDateTime.now());
            auctionRepository.save(getAuction);
        }
    }

    public boolean isAuctionActive(Auction auction) {
        if(auction.getQuantity() > 0 && auction.getEndDate().compareTo(LocalDateTime.now()) > 0) {
            return true;
        }

        return false;
    }

    public void deleteAuction(int id) {
        //getAuctionById check if the Auction exist in the database, if not throw NotFoundException (404 not found)
        Auction getAuction = getAuctionById(id);
        auctionRepository.delete(getAuction);
    }
}
