package com.phildev.pcs.service;


import com.phildev.pcs.domain.BidList;
import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.repositories.BidListRepository;
import com.phildev.pcs.repositories.CurvePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * This class is the service used to communicate between {@link com.phildev.pcs.controllers.CurveController} and {@link com.phildev.pcs.repositories.BidListRepository}
 * All the methods in this service helps saving, deleting, retrieving curve point in database
 */
@Service
public class CurvePointService {

    private static final Logger logger = LoggerFactory.getLogger(CurvePointService.class);

    @Autowired
    private CurvePointRepository curvePointRepository;


    /**
     * This method calls bid repository with its {@link CurvePointRepository#findAll()} method to find all curve points in database
     * @return a list of CurvePoint
     */
    public List<CurvePoint> findAll(){
        List<CurvePoint> allCurvePoints = curvePointRepository.findAll();
        if(allCurvePoints.isEmpty()){
            logger.info("Curve point list is empty");
        }
        return allCurvePoints;
    }


    /**
     * This method saves a {@link CurvePoint} in database
     * @param curvePoint with its properties term , value that will be saved in database
     * @return a curve point which was saved successfully in database
     */
    public CurvePoint save(CurvePoint curvePoint){
        return curvePointRepository.save(curvePoint);
    }

    /**
     * This method takes the id of a curve point we want to find and the method {@link CurvePointRepository#findById(Object)} will retrieve the curve point or not owing to Optional type
     * @param id which is the unique curve point identifier
     * @return an optional of curvePoint so it can be empty or return something
     */
    public Optional<CurvePoint> findById(Integer id){
        return curvePointRepository.findById(id);
    }


    /**
     * This method takes the id of a curve point we want to delete and the method {@link CurvePointRepository#deleteById(Object)} will delete the curve point
     * @param id which is the unique curve point identifier
     * @return void
     */
    public void delete(Integer id){
        curvePointRepository.deleteById(id);
    }
}
