package com.trakntell.web.repositories;

import com.trakntell.web.models.TNTOAuthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<TNTOAuthAccessToken, String> {

    /*@Modifying
    @Query("update TNTOAuthAccessToken set token = '' where user_name = :user")
    int setTokenBlankForUser(@Param("user") String user);*/

}
