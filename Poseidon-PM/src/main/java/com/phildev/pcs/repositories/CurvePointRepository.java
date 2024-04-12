package com.phildev.pcs.repositories;

import com.phildev.pcs.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

    @Query( value="SELECT cv FROM CurvePoint cv WHERE cv.curveId=:curvedId",
            nativeQuery = false
    )
    public Optional<CurvePoint> findByCurveId(@Param("curvedId") Integer id);

}
