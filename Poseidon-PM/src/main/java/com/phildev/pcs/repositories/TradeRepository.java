package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
