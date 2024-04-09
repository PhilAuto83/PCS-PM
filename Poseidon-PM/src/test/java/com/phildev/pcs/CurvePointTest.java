package com.phildev.pcs;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.repositories.CurvePointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
public class CurvePointTest {

	@Autowired
	private CurvePointRepository curvePointRepository;

	@Test
	public void curvePointSaveTest() {
		CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);
		// Save
		curvePoint = curvePointRepository.save(curvePoint);
		Assertions.assertNotNull(curvePoint.getId());
		Assertions.assertTrue(curvePoint.getCurveId() == 10);
	}

	@Test
	public void curvePointUpdateTest() {
		CurvePoint curvePoint2 = new CurvePoint(11, 11d, 30d);
		// Update
		curvePoint2.setCurveId(20);
		curvePoint2 = curvePointRepository.save(curvePoint2);
		Assertions.assertTrue(curvePoint2.getCurveId() == 20);
	}

	@Test
	public void curvePointSearchTest() {
		// Find
		List<CurvePoint> listResult = curvePointRepository.findAll();
		Assertions.assertTrue(listResult.size() > 0);
	}


	@Test
	public void curvePointDeleteTest() {
		CurvePoint curvePoint3 = new CurvePoint(11, 11d, 30d);
		// Delete
		Integer id = curvePoint3.getId();
		curvePointRepository.delete(curvePoint3);
		Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
		Assertions.assertFalse(curvePointList.isPresent());
	}

}
