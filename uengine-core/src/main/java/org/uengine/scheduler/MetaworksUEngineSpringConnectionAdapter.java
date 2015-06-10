package org.uengine.scheduler;

import java.sql.Connection;

import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.uengine.util.dao.DefaultConnectionFactory;

public class MetaworksUEngineSpringConnectionAdapter extends DefaultConnectionFactory{
	
//	org.metaworks.spring.SpringConnectionFactory metaworksSpringConnectionFactory;
//		public org.metaworks.spring.SpringConnectionFactory getMetaworksSpringConnectionFactory() {
//			return metaworksSpringConnectionFactory;
//		}
//		public void setMetaworksSpringConnectionFactory(
//				org.metaworks.spring.SpringConnectionFactory metaworksSpringConnectionFactory) {
//			this.metaworksSpringConnectionFactory = metaworksSpringConnectionFactory;
//		}

//	@Autowired
//	org.metaworks.spring.SpringConnectionFactory metaworksSpringConnectionFactory;


//	@Autowired
//	MetaworksRemoteService metaworksRemoteService;
//	
	public Connection getConnection() throws Exception{
		
		return TransactionContext.getThreadLocalInstance().getConnection();
		//return metaworksRemoteService.getConnectionFactory().getConnection();
	}

		
}
