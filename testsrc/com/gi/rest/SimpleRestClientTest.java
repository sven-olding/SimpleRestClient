package com.gi.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

public class SimpleRestClientTest
{

	@Test
	public void testDoGet() throws ClientProtocolException, IOException 
	{
		String response = new SimpleRestClient().doGet("https://www.google.com", new HashMap<String, String>());
		assertThat(response, is(not(nullValue())));
	}
	
	@Test(expected = IOException.class)
	public void testDoGetWithIOException() throws ClientProtocolException, IOException
	{
		new SimpleRestClient().doGet("https://foo.doesntexist.com", null);
	}
}
