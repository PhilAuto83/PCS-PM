package com.phildev.pcs.service;


import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.repositories.CurvePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {

    private static final Logger logger = LoggerFactory.getLogger(CurvePointService.class);

    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> findAll(){
        List<CurvePoint> allCurvePoints = curvePointRepository.findAll();
        if(allCurvePoints.isEmpty()){
            logger.info("Curve point list is empty");
        }
        return allCurvePoints;
    }

    public CurvePoint save(CurvePoint curvePoint){
        return curvePointRepository.save(curvePoint);
    }

    public Optional<CurvePoint> findById(Integer id){
        return curvePointRepository.findById(id);
    }


    public void delete(Integer id){
        curvePointRepository.deleteById(id);
    }
}
