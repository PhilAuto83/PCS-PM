package com.phildev.pcs.service;

import com.phildev.pcs.domain.Trade;
import com.phildev.pcs.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> findAll(){
        List<Trade> allTrades = tradeRepository.findAll();
        if(allTrades.isEmpty()){
            logger.info("Curve point list  is empty");
        }
        return allTrades;
    }

    public Trade save(Trade trade){
        logger.info("Starting to save trade with account {} and type {}", trade.getAccount(), trade.getType());
        return tradeRepository.save(trade);
    }

    public Optional<Trade> findById(Integer id){
        return tradeRepository.findById(id);
    }

    public Optional<Trade> findTradeByAccount(String account){
        return tradeRepository.findTradeByAccount(account);
    }

    public void delete(Integer id){
        tradeRepository.deleteById(id);
    }
}
