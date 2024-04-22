package com.phildev.pcs.service;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.domain.RuleName;
import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.repositories.BidListRepository;
import com.phildev.pcs.repositories.RuleNameRepository;
import com.phildev.pcs.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * This class is the service used to communicate between {@link com.phildev.pcs.controllers.TradeController} and {@link TradeRepository}
 * All the methods in this service helps saving, deleting, retrieving Trade in database
 */
@Service
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private BidListRepository bidListRepository;


    /**
     * This method calls {@link TradeRepository#findAll()} method to find all trades in database
     * @return a list of Trade
     */
    public List<Trade> findAll(){
        List<Trade> allTrades = tradeRepository.findAll();
        if(allTrades.isEmpty()){
            logger.info("Curve point list  is empty");
        }
        return allTrades;
    }

    /**
     * This method is called to retrieve all bids found by account
     * It is used to see if a trade matches a bid
     * @param account
     * @return a list of bid
     */
    public List<BidList>findBidListByAccount(String account){
        return bidListRepository.findBidListByAccount(account);
    }

    /**
     * This method is used to check if a bid exists with the same account, type and quantity
     * @param trade which is an object of type {@link Trade}
     * @return true or false
     */
    public boolean checkBidExistWithAccountQuantityAndType(Trade trade){
        List<BidList> bidListRelatedToTradeAccount = findBidListByAccount(trade.getAccount());
        if(bidListRelatedToTradeAccount.isEmpty()){
            return false;
        }
        for(BidList bidList : bidListRelatedToTradeAccount){
            if(bidList.getType().equals(trade.getType())&& bidList.getBidQuantity().equals(trade.getBuyQuantity())){
                return true;
            }
        }
        return false;
    }

    /**
     * This method saves a {@link Trade} in database
     * @param trade with its properties {@link Trade#account}, {@link Trade#type},
     * and {@link Trade#buyQuantity} that will be saved in database
     * @return a trade which was saved successfully in database
     */
    public Trade save(Trade trade){
        logger.info("Starting to save trade with account {} and type {}", trade.getAccount(), trade.getType());
        return tradeRepository.save(trade);
    }

    /**
     * This method takes the id of a trade we want to find and the method {@link TradeRepository#findById(Object)} will retrieve the trade or not owing to Optional type
     * @param id which is the unique trade identifier
     * @return an optional of trade so it can be empty or return something
     */
    public Optional<Trade> findById(Integer id){
        return tradeRepository.findById(id);
    }


    /**
     * This method is used to retrieve a list of trade by passing an account parameter
     * @param account
     * @return a list of trade
     */
    public List<Trade> findTradeByAccount(String account){
        return tradeRepository.findTradeByAccount(account);
    }

    /**
     * This method takes the id matching a trade we want to delete and the method {@link Trade#deleteById(Object)} will delete the trade
     * @param id which is the unique trade identifier
     * @return void
     */
    public void delete(Integer id){
        tradeRepository.deleteById(id);
    }
}
