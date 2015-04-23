package org.uengine.contexts;

import java.io.Serializable;
import java.util.ArrayList;

import org.metaworks.FieldDescriptor;
import org.metaworks.ObjectType;
import org.metaworks.Type;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.PropertyListable;
import org.uengine.processmanager.ProcessManagerRemote;

public class ComplexType implements Serializable, PropertyListable{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
	}

	String typeId;
	Object value;
	transient Class typeClass;
	ClassLoader classLoader;
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Class getTypeClass() throws Exception{
		return getTypeClass(null);
	}

	public Class getTypeClass(ProcessManagerRemote pm) throws Exception{
		if(typeClass!=null) return typeClass;
	
		
		
//		String clsTypeId = ProcessDefinition.splitDefinitionAndVersionId(getTypeId())[1];
		
		String className = getTypeId().substring(1, getTypeId().lastIndexOf(".")).replace('/', '.');
		
		typeClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		
		return typeClass;
		

//		InputStream is = null;
//		String javaSource = null;
//		
//		if(GlobalContext.isDesignTime()){
//			is = ProcessDesigner.getClientProxy().showObjectDefinitionWithDefinitionId(clsTypeId);
//			ByteArrayOutputStream bao = new ByteArrayOutputStream();
//			UEngineUtil.copyStream(is, bao);
//			javaSource = bao.toString();
//		}else{
//			javaSource = pm.getResource(clsTypeId);
//		}
//		
//        Java.CompilationUnit cu;
//        
//        cu = new Parser(new Scanner("", new ByteArrayInputStream(javaSource.getBytes()))).parseCompilationUnit();
//        final ArrayList clsNames = new ArrayList();
//
//        // Traverse it and count declarations.
//        new Traverser(){
//			public void traverseClassDeclaration(ClassDeclaration arg0) {
//				clsNames.add(arg0.getClassName());
//				super.traverseClassDeclaration(arg0);
//			}
//        }.traverseCompilationUnit(cu);
//
//        String clsName = (String)clsNames.get(0);
//        
//        SimpleCompiler compiler = new SimpleCompiler();
//		compiler.setParentClassLoader(GlobalContext.class.getClassLoader());
//		compiler.cook(new ByteArrayInputStream(javaSource.getBytes()));
//		//compiler.getClassLoader().
//		
//		classLoader = compiler.getClassLoader();
//		typeClass = classLoader.loadClass(clsName);
		
//		return typeClass;
	}
	
	
	public ArrayList<String> listProperties() {
		try {
			Class clazz = getTypeClass();
			
			ObjectType type = new ObjectType(clazz);
			ArrayList<String> fieldNames = new ArrayList<String>();
			for(FieldDescriptor fd: type.getFieldDescriptors()){
				fieldNames.add(fd.getName());
			}
			
			return fieldNames;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);//.printStackTrace();
		}
	}

}
