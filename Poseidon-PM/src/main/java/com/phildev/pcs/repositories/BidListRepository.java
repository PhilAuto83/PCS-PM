package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BidListRepository extends JpaRepository<BidList, Integer> {

    List<BidList> findBidListByAccount(String account);

    List<BidList> findBidListByAccountAndType(String account , String type);


}
