package com.trakntell.web.rest.graphql.request;

import java.util.Map;

public class GraphQLRequest {
	private String query;
	private Map<String, Object> variables;
	private String authorization;

	public GraphQLRequest(String query, Map<String, Object> variables, String authorization) {
		this.setQuery(query);
		this.setVariables(variables);
		this.setAuthorization(authorization);
	}

	public GraphQLRequest(String query, Map<String, Object> variables) {
		this.setQuery(query);
		this.setVariables(variables);
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the variables
	 */
	public Map<String, Object> getVariables() {
		return variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	/**
	 * @return the authorization
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * @param authorization the authorization to set
	 */
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
}
