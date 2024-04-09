package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {

}
