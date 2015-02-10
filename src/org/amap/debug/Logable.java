package org.amap.debug;

public interface Logable {

	public String getTag();
	
	public void debug(String message);
	
	public boolean isDebuging();
}
