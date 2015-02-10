package org.amap.overlay;

import org.amap.debug.Logable;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps.model.Marker;

public abstract class Overlay  implements Logable{
	public abstract void addToMap();
	public abstract void removeFromMap();
	
	/**
	 *  you must override this method,so you can distinguish different markers  from each other.
	 * @param marker
	 * @return
	 */
	public boolean isDrawnOnTheOverlay(Marker marker) {
		return false;
	}
	
	/**
	 *  you can override this method to get your  customerized  tag.
	 */
	@Override
	public String getTag() {
		return "Overlay";
	}
	
	@Override
	public void debug(String message) {
		if(!isDebuging()){
			return;
		}
		
		
	   if(TextUtils.isEmpty(message)){
		   return;
	   }
	   
	   Log.d(getTag(), message);
	}
	
	/**
	 *  control whether to print the debug logs
	 */
	@Override
	public boolean isDebuging() {
		return false;
	}
}
