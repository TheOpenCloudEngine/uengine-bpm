package org.uengine.contexts;

import java.util.ArrayList;
import java.util.List;

import org.metaworks.ToAppend;
import org.metaworks.component.TreeNode;
import org.metaworks.dao.IDAO;
import org.metaworks.dao.MetaworksDAO;
import org.metaworks.dao.TransactionContext;
import org.uengine.kernel.Role;

public class RoleTreeNode extends TreeNode {
	
	public void load(List<Role> roleList){
		this.setName("roles");
		this.setLoaded(true);
		this.setExpanded(true);
		
		for(int i = 0; i < roleList.size(); i++){
			Role role = roleList.get(i);			
			
			RoleTreeNode node = new RoleTreeNode();			
			// 주의 : id에 "." 이 들어간다면 Tree 에서 Id검색을 할수가 없다. 그리하여 "-" 으로 데이터 셋팅함
			node.setId("[roles]-" + role.getName());
			node.setName(role.getDisplayName() == null ? role.getName() : role.getDisplayName().getText());
			node.setParentId(this.getId());
			node.setAlign(this.getAlign());
			node.setType(TreeNode.TYPE_FILE_HTML);
			
			this.add(node);
		}
	}
	
	public ArrayList<TreeNode> loadExpand() throws Exception{
		
		ArrayList<TreeNode> child = new ArrayList<TreeNode>();
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT roleCode, descr");
		sb.append("  FROM roleTABLE");
		//sb.append("WHERE ISDELETED = '0'");
		
		IDAO dao = (IDAO)MetaworksDAO.createDAOImpl(TransactionContext.getThreadLocalInstance().getConnectionFactory(), sb.toString(), IDAO.class);
		dao.set("ISDELETED", "0");
		dao.select();
		
		while(dao.next()){
			GroupTreeNode node = new GroupTreeNode();
			node.setId(dao.getString("roleCode"));
			node.setName(dao.getString("descr"));
			node.setParentId(this.getId());
			node.setType(TreeNode.TYPE_FILE_HTML);
			
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
