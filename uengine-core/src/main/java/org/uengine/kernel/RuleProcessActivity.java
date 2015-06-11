/*
 * Created on 2004-04-12
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.kernel;

import org.metaworks.Type;

public class RuleProcessActivity extends SubProcessActivity {
  private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
  
  public static void metaworksCallback_changeMetadata(Type type){

	  SubProcessActivity.metaworksCallback_changeMetadata(type);
  }

  public RuleProcessActivity(){
    super();
    setName("rule process");
  }
  

}


