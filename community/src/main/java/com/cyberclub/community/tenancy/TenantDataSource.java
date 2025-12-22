package com.cyberclub.community.tenancy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import com.cyberclub.community.context.TenantContext;

public class TenantDataSource extends DelegatingDataSource {

    public TenantDataSource ( DataSource targetSource){
        super(targetSource);
    }

    private static final Logger log = LoggerFactory.getLogger(TenantDataSource.class);

    @Override
    public Connection getConnection() throws SQLException{
        Connection connection = super.getConnection();
        try {
            applySchema(connection);
            return connection;
        } catch (SQLException ex) {
            connection.close();
            throw ex;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException{
        Connection connection = super.getConnection(username, password);
        try{
            applySchema(connection);
            return connection;
        } catch (SQLException ex){
            connection.close();
            throw ex;
        }
    }

    private void applySchema(Connection connection) throws SQLException{
        if(!TenantContext.isSet()){
            throw new IllegalStateException("Tenant Context must be set before accessing data source");
        }

        String tenant = TenantContext.get();
        String schema = "tenant_" + tenant;

        log.debug("Setting search_path to schema [{}]" , schema);
        try(Statement stmt = connection.createStatement()){
            stmt.execute("SET search_path TO \"" + schema + "\", public");
        }
    }


    
}
