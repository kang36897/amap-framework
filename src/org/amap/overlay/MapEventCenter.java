package org.amap.overlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.amap.R;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMapTouchListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

public class MapEventCenter implements OnMarkerClickListener, OnMapTouchListener, OnMarkerDragListener, InfoWindowAdapter, OnInfoWindowClickListener,
		OnMapClickListener, OnMapLongClickListener, OnMapLoadedListener {

	private List<OnMapTouchListener> mapTouchListeners = new LinkedList<OnMapTouchListener>();
	private List<OnMarkerClickListener> markerClickListeners = new LinkedList<OnMarkerClickListener>();
	private List<OnMarkerDragListener> markerDragListeners = new LinkedList<OnMarkerDragListener>();
	private List<InfoWindowAdapter> infoWindowAdapters = new LinkedList<InfoWindowAdapter>();
	private List<OnInfoWindowClickListener> infoWindowClickListeners = new LinkedList<OnInfoWindowClickListener>();
	private List<OnMapClickListener> mapClickListeners = new LinkedList<OnMapClickListener>();
	private List<OnMapLongClickListener> mapLongClickListeners = new LinkedList<OnMapLongClickListener>();
	private List<OnMapLoadedListener> mapLoadedListeners = new LinkedList<AMap.OnMapLoadedListener>();
	private List<Overlay> mOverlays = new ArrayList<Overlay>();

	private AMap aMap;
	private Context mContext;

	public MapEventCenter(Context context) {
		mContext = context;
	}

	public void takeControl(AMap aMap) {
		this.aMap = aMap;

		aMap.setOnMapTouchListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnMarkerDragListener(this);
		aMap.setInfoWindowAdapter(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setOnMapClickListener(this);
		aMap.setOnMapLongClickListener(this);
		aMap.setOnMapLoadedListener(this);
	}

	public AMap getAMap() {
		return aMap;
	}

	public void addOnMarkerClickListener(OnMarkerClickListener l) {
		if (markerClickListeners.contains(l)) {
			return;
		}

		markerClickListeners.add(l);
	}

	public void removeOnMarkerClickListener(OnMarkerClickListener l) {
		markerClickListeners.remove(l);
	}

	public void addOnMarkerDragListener(OnMarkerDragListener l) {
		if (markerDragListeners.contains(l)) {

		}

		markerDragListeners.add(l);
	}

	public void removeOnMarkerDragListener(OnMarkerDragListener l) {
		markerDragListeners.remove(l);
	}

	public void addInfoWindowAdapter(InfoWindowAdapter adapter) {
		if (infoWindowAdapters.contains(adapter)) {
			return;
		}

		infoWindowAdapters.add(adapter);
	}

	public void removeInfoWindowAdapter(InfoWindowAdapter adapter) {
		infoWindowAdapters.remove(adapter);
	}

	public void addOnInfoWindowClickListener(OnInfoWindowClickListener l) {
		if (infoWindowClickListeners.contains(l)) {
			return;
		}

		infoWindowClickListeners.add(l);
	}

	public void removeOnInfoWindowClickListener(OnInfoWindowClickListener l) {
		infoWindowClickListeners.remove(l);
	}

	public void addOnMapClickListener(OnMapClickListener l) {
		if (mapClickListeners.contains(l)) {
			return;
		}

		mapClickListeners.add(l);
	}

	public void removeOnMapClickListener(OnMapClickListener l) {
		mapClickListeners.remove(l);
	}

	public void addOnMapLongClickListener(OnMapLongClickListener l) {
		if (mapLongClickListeners.contains(l)) {
			return;
		}

		mapLongClickListeners.add(l);
	}

	public void removeOnMapLongClickListener(OnMapLongClickListener l) {
		mapLongClickListeners.remove(l);
	}

	public void addOnMapTouchListener(OnMapTouchListener l) {
		if (mapTouchListeners.contains(l)) {
			return;
		}

		mapTouchListeners.add(l);
	}

	public void removeOnMapTouchListener(OnMapTouchListener l) {
		mapTouchListeners.remove(l);
	}

	public void addOnMapLoadedListener(OnMapLoadedListener l) {
		mapLoadedListeners.add(l);
	}

	public void removeOnMapLoadedListener(OnMapLoadedListener l) {
		mapLoadedListeners.remove(l);
	}

	@Override
	public void onMapLongClick(LatLng paramLatLng) {
		for (OnMapLongClickListener listener : mapLongClickListeners) {
			listener.onMapLongClick(paramLatLng);
		}
	}

	@Override
	public void onMapClick(LatLng paramLatLng) {
		for (OnMapClickListener listener : mapClickListeners) {
			listener.onMapClick(paramLatLng);
		}
	}

	@Override
	public void onInfoWindowClick(Marker paramMarker) {
		for (OnInfoWindowClickListener listener : infoWindowClickListeners) {
			listener.onInfoWindowClick(paramMarker);
		}
	}



	/**
	 *  //-------------------------------------begin --------------------------------------------------
	 *   After upgrade to v2.0 , there is only one info window on the map at one time. If you want 
	 *   to show info window ,you must implments InfoWindowAdapter. Reusing the inflated view
	 *   is a good idea when you don't want to inflate the xml every time. Unexpectedly it will throw 
	 *   exception when you show the info window of another marker on the same layer. 
	 *   
	 *    As a result, i wrap each view with a LinearLayout as parent. So you can avoid this  before 
	 *    assign new values to this one which has been initialed before,just do it as follow:
	 *    
	 *         ViewParent vp =	view.getParent();
     *               if(vp != null){
     *	             ViewGroup vg = (ViewGroup)vp;
     *                vg.removeView(targetView);
      *            }
	 */
	
	/**
	 *  you can change the background of info window for a special layer, just implements this interface.
	 * 
	 *
	 */
	public static interface WrapperCreator {
		public ViewGroup getWrapper(View view);
	}
	
	
	/**
	 *  you can change the background of info window ,just override this method.
	 * @param view
	 * @return
	 */
	private View packWrapper(View view) {
		LinearLayout wrapper = new LinearLayout(mContext);
		wrapper.setOrientation(LinearLayout.VERTICAL);
		wrapper.setBackgroundResource(R.drawable.tip_pointer_button_normal);
		wrapper.addView(view, new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		return wrapper;
	}

	@Override
	public View getInfoWindow(Marker paramMarker) {
		for (InfoWindowAdapter adapter : infoWindowAdapters) {
			View view = adapter.getInfoWindow(paramMarker);
			if (view != null) {

				if (adapter instanceof WrapperCreator) {
					return ((WrapperCreator) adapter).getWrapper(view);
				}

				return packWrapper(view);
			}
		}

		return null;
	}

	@Override
	public View getInfoContents(Marker paramMarker) {
		for (InfoWindowAdapter adapter : infoWindowAdapters) {
			View view = adapter.getInfoContents(paramMarker);
			if (view != null) {

				if (adapter instanceof WrapperCreator) {
					return ((WrapperCreator) adapter).getWrapper(view);
				}

				return packWrapper(view);
			}
		}

		return null;
	}

	/**
	 *  //-------------------------------------end --------------------------------------------------
	 */
	
	
	@Override
	public void onMarkerDragStart(Marker paramMarker) {
		for (OnMarkerDragListener listener : markerDragListeners) {
			listener.onMarkerDragStart(paramMarker);
		}
	}

	@Override
	public void onMarkerDrag(Marker paramMarker) {
		for (OnMarkerDragListener listener : markerDragListeners) {
			listener.onMarkerDrag(paramMarker);
		}
	}

	@Override
	public void onMarkerDragEnd(Marker paramMarker) {
		for (OnMarkerDragListener listener : markerDragListeners) {
			listener.onMarkerDragEnd(paramMarker);
		}
	}

	@Override
	public boolean onMarkerClick(Marker paramMarker) {
		for (OnMarkerClickListener listener : markerClickListeners) {
			if (listener.onMarkerClick(paramMarker)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void onTouch(MotionEvent event) {
		for (OnMapTouchListener listener : mapTouchListeners) {
			listener.onTouch(event);
		}

	}

	@Override
	public void onMapLoaded() {
		for (OnMapLoadedListener listener : mapLoadedListeners) {
			listener.onMapLoaded();
		}

	}

	public void addToMap(Overlay overlay) {
		if (overlay == null) {
			return;
		}

		if (overlay instanceof OnMapTouchListener) {
			addOnMapTouchListener((OnMapTouchListener) overlay);
		} else if (overlay instanceof OnMarkerClickListener) {
			addOnMarkerClickListener((OnMarkerClickListener) overlay);
		} else if (overlay instanceof OnMarkerDragListener) {
			addOnMarkerDragListener((OnMarkerDragListener) overlay);
		} else if (overlay instanceof InfoWindowAdapter) {
			addInfoWindowAdapter((InfoWindowAdapter) overlay);
		} else if (overlay instanceof OnInfoWindowClickListener) {
			addOnInfoWindowClickListener((OnInfoWindowClickListener) overlay);
		} else if (overlay instanceof OnMapClickListener) {
			addOnMapClickListener((OnMapClickListener) overlay);
		} else if (overlay instanceof OnMapLongClickListener) {
			addOnMapLongClickListener((OnMapLongClickListener) overlay);
		} else if (overlay instanceof OnMapLoadedListener) {
			addOnMapLoadedListener((OnMapLoadedListener) overlay);
		}

		mOverlays.add(overlay);
	}

	public void removeFromMap(Overlay overlay) {
		if (overlay == null) {
			return;
		}

		if (overlay instanceof OnMapTouchListener) {
			removeOnMapTouchListener((OnMapTouchListener) overlay);
		} else if (overlay instanceof OnMarkerClickListener) {
			removeOnMarkerClickListener((OnMarkerClickListener) overlay);
		} else if (overlay instanceof OnMarkerDragListener) {
			removeOnMarkerDragListener((OnMarkerDragListener) overlay);
		} else if (overlay instanceof InfoWindowAdapter) {
			removeInfoWindowAdapter((InfoWindowAdapter) overlay);
		} else if (overlay instanceof OnInfoWindowClickListener) {
			removeOnInfoWindowClickListener((OnInfoWindowClickListener) overlay);
		} else if (overlay instanceof OnMapClickListener) {
			removeOnMapClickListener((OnMapClickListener) overlay);
		} else if (overlay instanceof OnMapLongClickListener) {
			removeOnMapLongClickListener((OnMapLongClickListener) overlay);
		} else if (overlay instanceof OnMapLoadedListener) {
			removeOnMapLoadedListener((OnMapLoadedListener) overlay);
		}

		mOverlays.remove(overlay);
	}

}
