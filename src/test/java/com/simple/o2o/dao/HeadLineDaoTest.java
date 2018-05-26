package com.simple.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.simple.o2o.BaseTest;
import com.simple.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	@Autowired
	private HeadLineDao headLineDao;
	
	@Test
	public void testQueryHeadLine(){
		HeadLine headLineCondition = new HeadLine();
		List<HeadLine> headLineList = headLineDao.queryHeadLine(headLineCondition);
		assertEquals(4, headLineList.size());
	}
}
