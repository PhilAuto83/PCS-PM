package com.phildev.pcs;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.service.CurvePointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class CurvePointTest {

	@Autowired
	private CurvePointService curvePointService;

	@Test
	public void curvePointSaveTest() {
		CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);
		// Save
		CurvePoint curvePointSaved = curvePointService.save(curvePoint);
		Assertions.assertNotNull(curvePointSaved.getId());
		Assertions.assertTrue(curvePointSaved.getCurveId() == 10);
	}

	@Test
	public void curvePointUpdateTest() {
		CurvePoint curvePoint2 = new CurvePoint(11, 11d, 30d);
		// Update
		curvePoint2.setCurveId(20);
		CurvePoint curveInDB = curvePointService.save(curvePoint2);
		Assertions.assertTrue(curveInDB.getCurveId() == 20);
	}

	@Test
	public void curvePointSearchTest() {
		CurvePoint curvePointSearch = new CurvePoint(45, 100d, 30d);
		// Save
		curvePointService.save(curvePointSearch);
		// Find
		List<CurvePoint> listResult = curvePointService.findAll();
        Assertions.assertFalse(listResult.isEmpty());
		long curveFound  = listResult.stream().filter(curvePoint -> curvePoint.getCurveId()==45).count();
        Assertions.assertEquals(1, curveFound);
	}


	@Test
	public void curvePointDeleteTest() {
		CurvePoint curvePoint3 = new CurvePoint(11, 11d, 30d);
		// Delete
		CurvePoint curveInDB = curvePointService.save(curvePoint3);
		curvePointService.delete(curveInDB.getId());
		Assertions.assertFalse(curvePointService.findById(curveInDB.getId()).isPresent());
	}

}
