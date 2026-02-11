package com.cyberclub.learn.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DelegatingDataSource;

public class ServiceDataSource extends DelegatingDataSource{

    public ServiceDataSource (DataSource targDataSource){
        super(targDataSource);
    }

    private static final Logger log = LoggerFactory.getLogger(ServiceDataSource.class);

    @Override
    public Connection getConnection() throws SQLException{
        return applySchemaAndReturn(super.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException{
        return applySchemaAndReturn(super.getConnection(username, password));
    }


    private Connection applySchemaAndReturn(Connection connection) throws SQLException {
         try {
             applySchema(connection);
             return connection;
         } catch (Exception ex) {
             connection.close();
            if (ex instanceof SQLException) {
                 throw (SQLException) ex;
             }
             throw new SQLException("Failed to apply service schema", ex);
         }
     }

    private void applySchema(Connection connection) throws SQLException{
        // if(!TenantContext.isSet()){
        //     throw new IllegalStateException("Tenant Context must be set before accessing data source");
        // }

        // String tenant = TenantContext.get();
        
        // Validate tenant identifier
        // if (!isValidPostgresIdentifier(tenant)) {
        //     throw new IllegalArgumentException("Invalid tenant identifier: " + tenant);
        // }
        
        String schema = "learn";

        log.debug("Setting search_path to schema [{}]" , schema);
        try(Statement stmt = connection.createStatement()){
            stmt.execute("SET search_path TO \"" + schema + "\", public");
        }
    }
}
