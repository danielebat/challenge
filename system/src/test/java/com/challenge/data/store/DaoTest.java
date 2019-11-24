package com.challenge.data.store;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.challenge.data.model.IJsonObject;

public class DaoTest {
	
	Dao<IJsonObject> dao;
	IJsonObject jsonObject;
	
	@Before
	public void setup() {
		
		dao = new Dao<IJsonObject>();
		jsonObject = mock(IJsonObject.class);
	}
	
	@Test
	public void testGivenEntityToPersistWhenAddingItThenEntityHasIdSet() {
		ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
		Integer result = dao.add(jsonObject);
		verify(jsonObject).setId(captor.capture());
		assertEquals(captor.getValue(), result);
	}

}
