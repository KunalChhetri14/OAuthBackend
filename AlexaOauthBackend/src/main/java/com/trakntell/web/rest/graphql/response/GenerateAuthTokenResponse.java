package com.trakntell.web.rest.graphql.response;

import java.util.Map;

public class GenerateAuthTokenResponse {
	private Map<String, Object> data;

	/**
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
