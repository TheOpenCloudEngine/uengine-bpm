package org.uengine.util.dao;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by jjy on 2016. 9. 17..
 */
public class UEngineSpringConnectionFactory  extends DefaultConnectionFactory {

    private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

    DataSource dataSource;
    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {

        Connection	conn = DataSourceUtils.getConnection(dataSource);
        return conn;
    }
}
