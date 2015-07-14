package org.uengine.contexts;

import java.util.ArrayList;

import org.metaworks.ToAppend;
import org.metaworks.component.TreeNode;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;

public class GroupTreeNode extends TreeNode {

	public ArrayList<TreeNode> loadExpand() throws Exception{
		
		ArrayList<TreeNode> child = new ArrayList<TreeNode>();
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT partcode, partname");
		sb.append("  FROM parttable");
		//sb.append("WHERE ISDELETED = '0'");
		
		IDAO dao = (IDAO)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance().getConnectionFactory(), sb.toString(), IDAO.class);
		dao.set("ISDELETED", "0");
		dao.select();
		
		while(dao.next()){
			GroupTreeNode node = new GroupTreeNode();
			node.setId(dao.getString("partCode"));
			node.setName(dao.getString("partName"));
			node.setParentId(this.getId());
			node.setType(TreeNode.TYPE_FILE_CSS);
			
			child.add(node);
		}
		
		this.setExpanded(true);
		this.setLoaded(true);
		
		return child;
	}
	
	@Override
	public Object expand() throws Exception{
		return new ToAppend(this, this.loadExpand());
	}
}
