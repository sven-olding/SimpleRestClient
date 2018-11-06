package com.gi.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SimpleRestClient
{

	/**
	 * Perform a GET request
	 * 
	 * @param url the url to GET
	 * @param requestProperties the request headers
	 * @return the response body
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String doGet(String url, HashMap<String, String> requestProperties)
			throws ClientProtocolException, IOException
	{
		HttpGet getRequest = new HttpGet(url);

		if (requestProperties != null) {
			Iterator<String> it = requestProperties.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				getRequest.addHeader(key, requestProperties.get(key));
			}
		}

		// Create a custom response handler
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException
			{
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					if (entity == null)
						return null;

					Header encodingHeader = entity.getContentEncoding();
					Charset encoding = encodingHeader == null ? Charset.forName("UTF-8")
							: Charsets.toCharset(encodingHeader.getValue());
					return EntityUtils.toString(entity, encoding);

				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};

		return executeRequest(getRequest, responseHandler);
	}

	/**
	 * Executes a given request
	 * 
	 * @param request the request
	 * @param responseHandler the response handler
	 * @return the response body as string
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected String executeRequest(HttpUriRequest request, ResponseHandler<String> responseHandler)
			throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			return httpClient.execute(request, responseHandler);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
