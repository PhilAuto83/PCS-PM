package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BidListRepository extends JpaRepository<BidList, Integer> {


}
