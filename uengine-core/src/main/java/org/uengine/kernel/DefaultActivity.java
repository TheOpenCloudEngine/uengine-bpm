package org.uengine.kernel;

import javax.naming.InitialContext;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Order;
import org.metaworks.component.MetaWorksComponentCenter;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IDocument;
import org.uengine.util.UEngineUtil;

/**
 * @author Jinyoung Jang
 */
//@Face(displayName="Unit 프로세스")
public class DefaultActivity extends Activity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public DefaultActivity(String name){
		setName(name);
		
		//createDocument();
	}
	public DefaultActivity(){
		this("");
	}

	protected void executeActivity(ProcessInstance instance) throws Exception{
		System.out.println("default activity::execute: " + getName());

		fireComplete(instance);
	}

	public InitialContext getInitialContext() throws javax.naming.NamingException {
		return new InitialContext();
	}

	IDocument document;

	@Order(value=2)
	@Face(options="hideLabel", values="true")
	//@Valid
	public IDocument getDocument() {
		return document;
	}
	public void setDocument(IDocument document) {
		this.document = document;
	}
	
	public void createDocument(){
		try {
			setDocument((IDocument)Thread.currentThread().getContextClassLoader().loadClass("org.uengine.essencia.model.DefaultActivityDocument").newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

