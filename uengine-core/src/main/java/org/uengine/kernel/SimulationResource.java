package org.uengine.kernel;

public class SimulationResource {

	String resourceName;
	int quantity;
	boolean isLimitedResource;
	
	public boolean isLimitedResource() {
		return isLimitedResource;
	}
	public void setLimitedResource(boolean isLimitedResource) {
		this.isLimitedResource = isLimitedResource;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
}
