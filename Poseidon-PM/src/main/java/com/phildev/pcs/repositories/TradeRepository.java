package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {

    public List<Trade> findTradeByAccount(String account);


}
