package a10;

public class Resource {

	private int resourceAmount = 0;
	
	public int getResource() {
		return resourceAmount;
	}
	
	public void setResource(int resourceSet) {
		resourceAmount = resourceSet;
	}
	
	public void addResource(int resourceAdd) {
		resourceAmount += resourceAdd;
	}
}
