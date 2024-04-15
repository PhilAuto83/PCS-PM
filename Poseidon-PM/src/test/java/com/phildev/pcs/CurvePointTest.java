package com.phildev.pcs;

import com.phildev.pcs.domain.CurvePoint;
import com.phildev.pcs.service.CurvePointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CurvePointTest {

	@Autowired
	private CurvePointService curvePointService;

	@Test
	public void curvePointSaveTest() {
		CurvePoint curvePoint = new CurvePoint(10d, 30d);
		// Save
		CurvePoint curvePointSaved = curvePointService.save(curvePoint);
		Assertions.assertNotNull(curvePointSaved.getId());
        Assertions.assertEquals(10d, curvePointSaved.getTerm());
	}

	@Test
	public void curvePointUpdateTest() {
		CurvePoint curvePoint2 = new CurvePoint(11d, 30d);
		// Update
		curvePoint2.setValue(20d);
		CurvePoint curveInDB = curvePointService.save(curvePoint2);
        Assertions.assertEquals(20d, curveInDB.getValue());
	}

	@Test
	public void curvePointSearchTest() {
		CurvePoint curvePointSearch = new CurvePoint(100d, 30d);
		// Save
		CurvePoint curvePointSaved = curvePointService.save(curvePointSearch);
		// Find

        org.assertj.core.api.Assertions.assertThat(curvePointSaved).isNotNull();
	}


	@Test
	public void curvePointDeleteTest() {
		CurvePoint curvePoint3 = new CurvePoint(11d, 30d);
		// Delete
		CurvePoint curveInDB = curvePointService.save(curvePoint3);
		curvePointService.delete(curveInDB.getId());
		Assertions.assertFalse(curvePointService.findById(curveInDB.getId()).isPresent());
	}

}
