package com.phildev.pcs.service;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.repositories.BidListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * This class is the service used to communicate between {@link com.phildev.pcs.controllers.BidListController} and {@link BidListRepository}
 * All the methods in this service helps saving, deleting, retrieving bid in database
 */
@Service
public class BidListService {

    private static final Logger logger = LoggerFactory.getLogger(BidListService.class);

    @Autowired
    private BidListRepository bidListRepository;

    /**
     * This method calls bid repository with its {@link BidListRepository#findAll()} method to find all bids in database
     * @return a list of BidList
     */
    public List<BidList> findAll(){
        List<BidList> allBids = bidListRepository.findAll();
        if(allBids.isEmpty()){
            logger.info("Bid list is empty");
        }
        return allBids;
    }

    /**
     * This method saves a {@link BidList} in database
     * @param bidList with its properties account , type and  bid quantity that will be saved in database
     * @return a bid  which was saved successfully in database
     */
    public BidList save(BidList bidList){
        return bidListRepository.save(bidList);
    }

    /**
     * This method takes the id of a bid we want to find and the method {@link BidListRepository#findById(Object)} will retrieve the bid or not owing to Optional type
     * @param id which is the unique bid identifier
     * @return an optional of bidList so it can be empty or return something
     */
    public Optional<BidList> findById(Integer id){
        return bidListRepository.findById(id);
    }

    /**
     * This method takes the id of a bid we want to delete and the method {@link BidListRepository#deleteById(Object)} will delete the bid
     * @param id which is the unique bid identifier
     * @return void
     */
    public void delete(Integer id){
        bidListRepository.deleteById(id);
    }

}
