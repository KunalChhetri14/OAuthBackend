package com.trakntell.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

//Controller for performing ddl and dml statements in database
@RestController
public class DbController {
    private static final Logger logger = LoggerFactory.getLogger(DbController.class);

    @Autowired
    DataSource dataSource;

    @GetMapping(value = "/createTable")
    public void createTable() {
        //String tableSql = "create table mobile_orgid_vehicleid(mobile varchar(10) primary key, orgid varchar(32) not null, vehicleid varchar(32) not null, created datetime, modified datetime);";
        String truncateTable1 = "truncate table oauth_refresh_token;";
        //String truncateTable2 = "truncate table oauth_code;";
        //String truncateTable3 = "truncate table oauth_access_token;";
        String alterColumn = "ALTER TABLE oauth_refresh_token ALTER COLUMN token VARBINARY(Max);";


        try(Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            //int rc = stmt.executeUpdate(tableSql);
            //logger.info("rc = {}", rc);

            int rc1 = stmt.executeUpdate(truncateTable1);
            logger.info("rc1 = {}", rc1);

            //int rc2 = stmt.executeUpdate(truncateTable2);
            //logger.info("rc2 = {}", rc2);

            //int rc3 = stmt.executeUpdate(truncateTable3);
            //logger.info("rc2 = {}", rc3);

            int rc4 = stmt.executeUpdate(alterColumn);
            logger.info("rc4 = {}", rc4);
        }
        catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    @GetMapping(value = "/recreateTable")
    public void reCreateTable() {
        String dropTable = "DROP TABLE oauth_refresh_token;";
        String createTable = "CREATE TABLE oauth_refresh_token (token_id varchar(255) DEFAULT NULL, token varbinary(Max) DEFAULT NULL, authentication varbinary(Max) DEFAULT NULL);";

        try(Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            //int rc = stmt.executeUpdate(tableSql);
            //logger.info("rc = {}", rc);

            int rc1 = stmt.executeUpdate(dropTable);
            logger.info("rc1 = {}", rc1);

            //int rc2 = stmt.executeUpdate(truncateTable2);
            //logger.info("rc2 = {}", rc2);

            //int rc3 = stmt.executeUpdate(truncateTable3);
            //logger.info("rc2 = {}", rc3);

            int rc4 = stmt.executeUpdate(createTable);
            logger.info("rc4 = {}", rc4);
        }
        catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}
