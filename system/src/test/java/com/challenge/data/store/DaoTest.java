package com.challenge.data.store;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.challenge.data.model.IdentityObject;

public class DaoTest {
	
	Dao<IdentityObject> dao;
	IdentityObject jsonObject;
	
	@Before
	public void setup() {
		
		dao = new Dao<IdentityObject>();
		jsonObject = mock(IdentityObject.class);
	}
	
	@Test
	public void testGivenEntityToPersistWhenAddingItThenEntityHasIdSet() {
		ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
		Integer result = dao.add(jsonObject);
		verify(jsonObject).setId(captor.capture());
		assertEquals(captor.getValue(), result);
	}

}
