package com.trakntell.web.rest.graphql;

import com.trakntell.web.rest.graphql.request.GraphQLRequest;
import com.trakntell.web.rest.graphql.response.BaseGraphqlResponse;
import com.trakntell.web.rest.graphql.response.GenerateAuthTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "graphqlClient", url = "https://p360uat.tvsmotor.com/gapi")
public interface GraphQLApiClient {

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	GenerateAuthTokenResponse query(@RequestBody GraphQLRequest request);

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	BaseGraphqlResponse request(@RequestBody GraphQLRequest request,
								@RequestHeader("Authorization") String authorizationHeader);

}