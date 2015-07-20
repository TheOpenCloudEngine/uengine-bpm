package org.uengine.webservice;

import java.io.Serializable;
import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("resource")
public class ResourceProperty implements Serializable{
	
	@XStreamAsAttribute
	String path;
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
	
	String description;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
	ArrayList<MethodProperty> methods;
		public ArrayList<MethodProperty> getMethods() {
			return methods;
		}
		public void setMethods(ArrayList<MethodProperty> methods) {
			this.methods = methods;
		}

	ArrayList<ResourceProperty> childResources;
		public ArrayList<ResourceProperty> getChildResources() {
			return childResources;
		}
		public void setChildResources(ArrayList<ResourceProperty> childResources) {
			this.childResources = childResources;
		}
		
	public ResourceProperty(){
		methods 	= new ArrayList<MethodProperty>();
		childResources 		= new ArrayList<ResourceProperty>();
	}
}
