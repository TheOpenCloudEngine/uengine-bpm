package org.uengine.kernel;


import org.metaworks.EventContext;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.contexts.TextContext;
import org.uengine.modeling.ElementView;

@Face(ejsPath="dwr/metaworks/genericfaces/CleanObjectFace.ejs")
public class ReferenceActivity extends DefaultActivity {

	String fullTracingTag;
	boolean original;
	boolean changed;
	transient DefaultActivity referencedActivity;
	
	public ReferenceActivity(){
		
	}

	@Hidden
	public String getFullTracingTag() {
		return fullTracingTag;
	}
	public void setFullTracingTag(String fullTracingTag) {
		this.fullTracingTag = fullTracingTag;
	}

	@Hidden
	public boolean isOriginal() {
		return original;
	}
	public void setOriginal(boolean original) {
		this.original = original;
	}
	
	@Hidden
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	//@Valid
	public DefaultActivity getReferencedActivity() {
		return referencedActivity;
	}
	public void setReferencedActivity(DefaultActivity referencedActivity) {
		this.referencedActivity = referencedActivity;
	}

	@Hidden
	@Override
	public String getName(){
		if(this.getReferencedActivity() == null)
			return this.name.getText();
		
		return this.getReferencedActivity().getName();
	}
	public void setName(String name){
		this.getReferencedActivity().setName(name);
	}
	
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		if(this.getReferencedActivity() != null)
			this.getReferencedActivity().executeActivity(instance);
	}
	

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ReferenceActivity)
			return this.getTracingTag().equals(((ReferenceActivity)obj).getTracingTag());
		
		return false;
	}
	public String toString(){
		return this.getReferencedActivity().getName() + "(" + getTracingTag() + ")";
	}

	public boolean isChangedName(Activity activity){
		return !this.getReferencedActivity().getName().equals(activity.getName());
	}
	
	@Hidden
	@ServiceMethod(callByContent=true, bindingHidden=true, eventBinding=EventContext.EVENT_CHANGE)
	public void change(){
		this.setChanged(true);
	}
}
