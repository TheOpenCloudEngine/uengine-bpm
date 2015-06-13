/*
 * Created on 2004. 11. 3.
 */
package org.uengine.persistence.dao;

import org.uengine.util.dao.*;

/**
 * @author Jinyoung Jang
 */

public interface KeyGeneratorDAO extends IDAO{

	public Number getKeyNumber();
	public void setKeyNumber(Number id);

}
