package com.application.alledrogo.Service;

import com.application.alledrogo.Exception.NotAcceptableException;
import com.application.alledrogo.Exception.NotFoundException;
import com.application.alledrogo.Model.Auction;
import com.application.alledrogo.Repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Auction> getAuctionsBySubCategoryId(int subCategoryId) {
        List<Auction> getAuctions = auctionRepository.findAllBySubCategoryId(subCategoryId);
        if(getAuctions.isEmpty()) {
            throw new NotFoundException(String.format("Not found any Auctions with subCategoryId: %s", subCategoryId));
        }

        return getAuctions;
    }

    public List<Auction> getUserAuctions(int ownerId) {
        List<Auction> getAuctions = auctionRepository.findAllByOwnerId(ownerId);
        if(getAuctions.isEmpty()) {
            throw new NotFoundException(String.format("Not found any Auctions with ownerId: %s", ownerId));
        }

        return getAuctions;
    }

    public List<Auction> getFilteredAuction(Optional<Integer> categoryId, Optional<Integer> subCategoryId, Optional<Integer> ownerId, Optional<String> title, Optional<Double> priceFrom, Optional<Double> priceTo, Optional<LocalDateTime> endDateFrom, Optional<LocalDateTime> endDateTo) {
        //getAuctions check if the any auction exist in the database, if not throw NotFoundException (404 not found)
        List<Auction> filteredAuctions = getAllAuctions();

        if(categoryId.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getSubCategory().getId() == categoryId.get()).collect(Collectors.toList());
        }

        if(subCategoryId.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getSubCategory().getId() == subCategoryId.get()).collect(Collectors.toList());
        }

        if(ownerId.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getOwnerId() == ownerId.get()).collect(Collectors.toList());
        }

        if(title.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getTitle().contains(title.get())).collect(Collectors.toList());
        }

        if(priceFrom.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getPrice() >= priceFrom.get()).collect(Collectors.toList());
        }

        if(priceTo.isPresent()) {
            filteredAuctions = filteredAuctions.stream().filter(auction -> auction.getPrice() <= priceTo.get()).collect(Collectors.toList());
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

    public Auction addAuction(Auction auction) {
        if(auction.getCategory() == null || auction.getSubCategory() == null || auction.getOwnerId() == 0 || auction.getTitle() == null || auction.getDescription() == null ||  auction.getPrice() == 0 || auction.getQuantity() == 0) {
            throw new NotAcceptableException("Category, SubCategory, OwnerId, Title, Description, Price, Quantity should not be empty");
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

        if(auction.getCategory() == null) {
            auction.setCategory(getAuction.getCategory());
        }

        if(auction.getSubCategory() == null) {
            auction.setSubCategory(getAuction.getSubCategory());
        }

        if(auction.getOwnerId() == 0) {
            auction.setOwnerId(getAuction.getOwnerId());
        }

        if(auction.getTitle() == null) {
            auction.setTitle(getAuction.getTitle());
        }

        if(auction.getDescription() == null) {
            auction.setDescription(getAuction.getDescription());
        }

        if(auction.getPrice() == 0) {
            auction.setPrice(getAuction.getPrice());
        }

        if(auction.getQuantity() == 0) {
            auction.setQuantity(getAuction.getQuantity());
        }

        if(auction.getStartDate() == null) {
            auction.setStartDate(getAuction.getStartDate());
        }

        if(auction.getEndDate() == null) {
            auction.setEndDate(getAuction.getEndDate());
        }

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
