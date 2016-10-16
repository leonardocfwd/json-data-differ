package com.waesst.jsondiffer.persistence;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.waesst.jsondiffer.configuration.AppContext;
import com.waesst.jsondiffer.model.Diff;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppContext.class})
public class DiffDaoImplTest {

	@Autowired
	DiffDao diffDao;
	
	@Test
	public void test_save_persists_data(){
		Diff diff = getDiffInstance();
		diffDao.save(diff);
		
		//I'm testing diffDao.load indirectly, makes no sense to create another unit test just for it.
		Diff persistedDiff = diffDao.load(diff.getId());
		Assert.assertNotNull(persistedDiff);
		Assert.assertEquals(diff, persistedDiff);
	}
	
	private Diff getDiffInstance(){
		Diff diff = new Diff();
		diff.setId("16102016");
		diff.setLeft(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97});
		diff.setRight(new byte[]{72, 101, 108, 108, 111, 32, 109, 121, 32, 110, 97});
		return diff;
	}
}
