package com.phildev.pcs.service;

import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.repositories.BidListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {

    private static final Logger logger = LoggerFactory.getLogger(BidListService.class);

    @Autowired
    private BidListRepository bidListRepository;

    public List<BidList> findAll(){
        List<BidList> allBids = bidListRepository.findAll();
        if(allBids.isEmpty()){
            logger.info("Bid list is empty");
        }
        return allBids;
    }

}
