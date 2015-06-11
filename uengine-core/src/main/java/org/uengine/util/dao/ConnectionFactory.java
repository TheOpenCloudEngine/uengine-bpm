/*
 * Created on 2004. 12. 15.
 */
package org.uengine.util.dao;

import javax.sql.*;

import java.io.Serializable;
import java.sql.*;
import javax.naming.*;

import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */
public interface ConnectionFactory extends Serializable {
	public Connection getConnection() throws Exception;
}
