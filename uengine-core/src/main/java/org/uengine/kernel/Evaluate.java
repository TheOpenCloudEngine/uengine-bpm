package org.uengine.kernel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.metaworks.*;
import org.metaworks.annotation.*;
import org.metaworks.annotation.Face;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.ModalWindow;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.face.GenericValueFace;
import org.uengine.kernel.face.ProcessVariableExpressionFace;
import org.uengine.processmanager.SimulatorTransactionContext;
import org.uengine.util.UEngineUtil;



/**
 * @author Kinam Jung, Jinyoung Jang
 */
public class Evaluate extends Condition{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	String key;
	protected ProcessVariable pv;
	Object val;
	String type;
		
	String condition;	// default
	
	// For Reflection	
	public Evaluate(){
	}

	public Evaluate(String key, Object val){
		this( key, "==", val);
	}
	
	public Evaluate(String key, String condition, Object val){
		setKey(key);
		setValue(val);
		setCondition(condition);
	}
	
	public Evaluate(String key, String condition, Object val, String description){
		setKey(key);
		setValue(val);
		setCondition(condition);
		getDescription().setText(description);
	}
	
	public Evaluate(ProcessVariable pv, String condition, Object val){
		this.key = pv.getName();
		this.val = val;
		this.pv = pv;
		this.condition = condition;
	}
	
	public Evaluate(ProcessVariable pv, String condition, Object val, String description){
		this.key = pv.getName();
		this.val = val;
		this.pv = pv;
		this.condition = condition;
		getDescription().setText(description);
	}

	public boolean isMet(ProcessInstance instance, String scope) throws Exception{
		Object returnVal = null;

		if(key==null && pv!=null){
			key = pv.getName();
		}

		if(!UEngineUtil.isNotEmpty(key)){
			return true; //ignore the condition
		}


		if( key.startsWith("[instance]")  ){
			returnVal = instance.getBeanProperty(key); 
		}else{
			returnVal = instance.getProcessDefinition().getProcessVariable(key).get(instance, "");
		}
		Object compareVal = val;
		
		String condition = this.condition.trim();
		
		if(compareVal==null){
			if(condition.equals("!=")){
				return (returnVal!=null);
			}else if(condition.equals("contains") || condition.equals("not contains")){
				return checkupContainsCompareVal(returnVal, compareVal);
			}else{		
				return (returnVal==null || "".equals(returnVal));
			}
		}
		
		if(returnVal==null){
			if(condition.equals("!=")){
				return (compareVal!=null);
			}else{
				return (compareVal==null);
			}
		}
		
		if( compareVal instanceof String && "date".equalsIgnoreCase(type)){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			compareVal = df.parse((String)compareVal);
		}else if( compareVal instanceof String && "variable".equalsIgnoreCase(type)){
			// TODO
			if( ((String)compareVal).startsWith("[instance]")  ){
				compareVal = instance.getBeanProperty(((String)compareVal)); 
			}else{
				String compareString = (String)val;
				compareVal = instance.getProcessDefinition().getProcessVariable(compareString).get(instance, "");
				if(compareVal instanceof ITool){
					ObjectInstance returnValObjInst = (ObjectInstance) MetaworksRemoteService.getInstance().getMetaworksType(compareVal.getClass().getName() ).metaworks2Type().createInstance();
					returnValObjInst.setObject(compareVal);
					FieldDescriptor fields[] = returnValObjInst.getFieldDescriptors();
					Object keyVal = null;
					String [] wholePartPath = compareString.replace('.','@').split("@");
					if( wholePartPath.length >= 2 ){
						Object rootObject = instance.getBeanProperty(wholePartPath[0]);
						String rootObjectName = wholePartPath[1] ;
						if( wholePartPath.length > 2 ){
							for(int j = 2 ; j < wholePartPath.length; j++){
								rootObjectName += "."+ wholePartPath[j];
							}
						}
						if( rootObject != null ){
							compareVal = UEngineUtil.getBeanProperty(rootObject, rootObjectName);
						}
					}else{
						for(int j=0; j<fields.length; j++){
							FieldDescriptor fd = fields[j];
							if( key.equalsIgnoreCase( compareVal.getClass().getSimpleName() + "." + fd.getName() ) ){
								compareVal = returnValObjInst.getFieldValue(fd.getName());
							}
						}
					}
				}
			}
		}
		// compare with ComplexType
		if(returnVal instanceof ITool){
			
			ObjectInstance returnValObjInst = (ObjectInstance) MetaworksRemoteService.getInstance().getMetaworksType(returnVal.getClass().getName() ).metaworks2Type().createInstance();
			returnValObjInst.setObject(returnVal);
			FieldDescriptor fields[] = returnValObjInst.getFieldDescriptors();
			Object keyVal = null;
			String [] wholePartPath = getKey().replace('.','@').split("@");
			if( wholePartPath.length >= 2 ){
				Object rootObject = instance.getBeanProperty(wholePartPath[0]);
				String rootObjectName = wholePartPath[1] ;
				if( wholePartPath.length > 2 ){
					for(int j = 2 ; j < wholePartPath.length; j++){
						rootObjectName += "."+ wholePartPath[j];
					}
				}
				if( rootObject != null ){
					keyVal = UEngineUtil.getBeanProperty(rootObject, rootObjectName);
				}
			}else{
				for(int j=0; j<fields.length; j++){
					FieldDescriptor fd = fields[j];
					if( key.equalsIgnoreCase( returnVal.getClass().getSimpleName() + "." + fd.getName() ) ){
						keyVal = returnValObjInst.getFieldValue(fd.getName());
					}
				}
			}
			if(keyVal != null){
				if( condition.equals( "!=")){
					return !( keyVal.equals(compareVal) );
				}
				//use default comparator
				if( condition.equals( "==")){
					return keyVal.equals(compareVal);
				}
				if(!( keyVal instanceof Comparable) && (condition.startsWith(">") || condition.startsWith("<")))
					throw new UEngineException("The value type ["+ returnVal.getClass() +"] cannot be compared.");

				if(keyVal instanceof Comparable){
					int compareResult = 0;
					try {
						/*
						 * example : 숫자형을 compareTo 했을때 데이터형이 달라서 String 으로 바꿔서
						 * 비교를 한다면 예로 9 와 10을 비교하면 음수가 나오는것이 아니라 양수가 나오게 된다.
						 * 따라서 숫자형일때는 그형에 맞춰서 비교를 하고 나머지일 경우는 객체 자체를 비교
						 * 그래도 에러가 난다면 문자열로 형변환한 다음에 비교를 하도록 수정하였다.
						 */
						if (compareVal instanceof Integer) {
							compareResult = ((Integer)Integer.parseInt(String.valueOf(keyVal))).compareTo((Integer)compareVal);
						} else if (compareVal instanceof Long) {
							compareResult = ((Long)Long.parseLong(String.valueOf(keyVal))).compareTo((Long)compareVal);
						} else if (compareVal instanceof Float) {
							compareResult = ((Float)Float.parseFloat(String.valueOf(keyVal))).compareTo((Float)compareVal);
						} else if (compareVal instanceof Double) {
							compareResult = ((Double)Double.parseDouble(String.valueOf(keyVal))).compareTo((Double)compareVal);
						} else {
							compareResult = ((Comparable)keyVal).compareTo(compareVal);	
						}
					} catch(Exception e) {
						compareResult = String.valueOf(keyVal).compareTo(String.valueOf(compareVal));
					}

					if( condition.equals(">")){
						return (compareResult>0);
					}
						
					if( condition.equals("<")){
						return (compareResult<0);
					}
					
					if( condition.equals(">=")){
						return (compareResult>0 || compareResult==0);
					}
						
					if( condition.equals("<=")){
						return (compareResult<0 || compareResult==0);
					}
				}
			}
		}	// end compare with ComplexType
		if(val instanceof ProcessVariable){
			ProcessVariable variable = (ProcessVariable)val;
			compareVal = instance.getProcessDefinition().getProcessVariable(variable.getName()).get(instance, "");
		}

		if ( compareVal instanceof Number &&  returnVal.getClass() == String.class) {
			String strReturnVal = (String)returnVal;
			if(strReturnVal.startsWith("$")){
				strReturnVal = strReturnVal.substring(1, strReturnVal.length());
			}
			
			strReturnVal = strReturnVal.replaceAll("\054","");
			
			if(strReturnVal.indexOf(".") == -1)
				returnVal = new Long(strReturnVal);
			else
				returnVal = new Double(strReturnVal);
		}
		
		if( condition.equals( "!="))
			return !( instance.get(scope, key).equals(compareVal) );

		//use default comparator
		if( condition.equals( "=="))
			return returnVal.equals(compareVal);
		
		if( condition.equals( "!="))
			return !returnVal.equals(compareVal);

		if(!(returnVal instanceof Comparable) && (condition.startsWith(">") || condition.startsWith("<")))
			throw new UEngineException("The value type ["+ returnVal.getClass() +"] cannot be compared.");

		if(returnVal instanceof Comparable){
			
			int compareResult = 0;
			try {
				/*
				 * example : 숫자형을 compareTo 했을때 데이터형이 달라서 String 으로 바꿔서
				 * 비교를 한다면 예로 9 와 10을 비교하면 음수가 나오는것이 아니라 양수가 나오게 된다.
				 * 따라서 숫자형일때는 그형에 맞춰서 비교를 하고 나머지일 경우는 객체 자체를 비교
				 * 그래도 에러가 난다면 문자열로 형변환한 다음에 비교를 하도록 수정하였다.
				 */
				if (compareVal instanceof Integer) {
					compareResult = ((Integer)Integer.parseInt(String.valueOf(returnVal))).compareTo((Integer)compareVal);
				} else if (compareVal instanceof Long) {
					compareResult = ((Long)Long.parseLong(String.valueOf(returnVal))).compareTo((Long)compareVal);
				} else if (compareVal instanceof Float) {
					compareResult = ((Float)Float.parseFloat(String.valueOf(returnVal))).compareTo((Float)compareVal);
				} else if (compareVal instanceof Double) {
					compareResult = ((Double)Double.parseDouble(String.valueOf(returnVal))).compareTo((Double)compareVal);
				} else {
					compareResult = ((Comparable)returnVal).compareTo(compareVal);	
				}
			} catch(Exception e) {
				compareResult = String.valueOf(returnVal).compareTo(String.valueOf(compareVal));
			}

			if( condition.equals(">")){
				return (compareResult>0);
			}
				
			if( condition.equals("<")){
				return (compareResult<0);
			}
			
			if( condition.equals(">=")){
				return (compareResult>0 || compareResult==0);
			}
				
			if( condition.equals("<=")){
				return (compareResult<0 || compareResult==0);
			}
			
		}		
		
		//
		return instance.get(scope, key).equals(compareVal);
	}
	
	public String toString(){

		if (getDescription().getText() != null && getDescription().getText().length() > 0)
			return getDescription().getText();

		Object k = key;

		String expression;

		if (pv != null){
			k = pv;
			expression = pv.getName();
		}else{
			expression = k.toString();
		}

		//String returnVal = "instance.get(\""+ expression + "\"). " + this.convertJavaCondition(condition, val.toString());
		String returnVal = "instance.get(\""+ expression + "\")" + this.convertJavaCondition(condition, val.toString());

		return returnVal;
	}

	private String convertJavaCondition(String condition, String val){
		//String returnVal = "equals(\"" + val + "\")";
		String returnVal = condition + "\"" + val + "\"";
		//add condtion type

		return returnVal;
	}

	@Face(faceClass = ProcessVariableExpressionFace.class)
	@Order(1)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Order(3)
	@Face(faceClass=GenericValueFace.class)
	public Object getValue(){
		return val;
	}
	public void setValue( Object value){
		this.val = value;
	}

	@Range(options={"==", "!=", ">=", ">", "<", "<=", "contains", "not contains"}
			, values={"==", "!=", ">=", ">", "<", "<=", "contains", "not contains"})
	@Order(2)
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Hidden
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ValidationContext validate(Map options){
		ValidationContext validationContext = super.validate(options);
		
//		Activity activity = (Activity)options.get("activity");
//		
//		Evaluate evaluate = this;
//		ProcessVariable pv = activity.getProcessDefinition().getProcessVariable(evaluate.getKey()); 
//		if(pv==null)
//			validationContext.addWarning(activity.getActivityLabel()+" unrecognized process variable '" + evaluate.key + "' used in a condition expression" );
//		else{
//			try{
//				Class valueType;
//				
//				if(evaluate.getValue() instanceof ProcessVariable){
//					valueType = activity.getProcessDefinition().getProcessVariable(((ProcessVariable)evaluate.getValue()).getName()).getType();
//				}else
//					valueType = evaluate.getValue().getClass();
//									
//				if(!valueType.isAssignableFrom(pv.getType())){
//					if(evaluate.getValue() instanceof java.lang.Number){
//						try{
//							pv.getType().asSubclass(Number.class);
//						}catch (Exception e) {
//							validationContext.addWarning(activity.getActivityLabel()+" value '" + evaluate.getValue() + "' is not comparable to process variable '" + evaluate.key + "'" );
//						}
//					}else{
//						validationContext.addWarning(activity.getActivityLabel()+" value '" + evaluate.getValue() + "' is not comparable to process variable '" + evaluate.key + "'" );
//					}
//					
//					
//					
//					
//				}
//			}catch(Exception e){
//			}
//		}
		return validationContext;		
	}
	
	public static void main(String[] args) throws Exception{
		ProcessDefinition proc = new ProcessDefinition();
		proc.setProcessVariables(new ProcessVariable[]{
			ProcessVariable.forName("testvar")
		});
		
		ProcessInstance inst = new DefaultProcessInstance();
		inst.setProcessTransactionContext(new SimulatorTransactionContext());
		inst.setProcessDefinition(proc);
		inst.set("testvar", new Date(9));
		
		Evaluate cond = new Evaluate("testvar", "<", new Date(10));
		cond.isMet(inst, "");
	}
	
	private boolean checkupContainsCompareVal(Object returnVal, Object compareVal){
		
		boolean contain = false;
		
		if(returnVal instanceof ProcessVariableValue){
			ProcessVariableValue pvv = (ProcessVariableValue) returnVal; 
			do{
				Object unitVal = pvv.getValue();
				if(unitVal instanceof String && compareVal instanceof String){
					if(((String) compareVal).equals((String)unitVal))
							contain = true;
				}
				
			}while(pvv.next());
			
		}
		
		return contain;
	}


	@ServiceMethod(target = ServiceMethodContext.TARGET_POPUP, callByContent = true, inContextMenu = true)
	public ModalWindow edit(@AutowiredFromClient ProcessVariablePanel processVariablePanel){
		ModalWindow modalWindow = new ModalWindow(this);
		modalWindow.setMetaworksContext(new MetaworksContext());
		modalWindow.getMetaworksContext().setWhen(MetaworksContext.WHEN_EDIT);
		modalWindow.getMetaworksContext().setHow("full-fledged");

		return modalWindow;
	}


}