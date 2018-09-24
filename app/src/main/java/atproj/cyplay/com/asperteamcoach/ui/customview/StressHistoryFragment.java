package atproj.cyplay.com.asperteamcoach.ui.customview;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.StressInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.Stress;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.dagger.App;
import atproj.cyplay.com.asperteamcoach.ui.IHistory;

/**
 * Created by andre on 12-Jun-18.
 */

public class StressHistoryFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Inject
    StressInteractor stressInteractor;

    private GoogleMap googleMap;

    private boolean _created = false;
    private String _id;
    private List<Marker> _markers = new ArrayList<>();
    private String _startTime;
    private String _endTime;

    public void setId(String value) {
        _id = value;
    }

    public void setStartTime(String value) {
        _startTime = value;
    }

    public void setEndTime(String value) {
        _endTime = value;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        injectDependencies();

        getMapAsync(this);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        ((IHistory)getActivity()).setStressHistoryFragment(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        _created = true;
        if (_startTime != null && _endTime != null)
            getStresses(_id, _startTime, _endTime);
    }

    public void loadByPeriod() {
        if (_created)
            getStresses(_id, _startTime, _endTime);
    }

    protected void getStresses(String userId, String startTime, String endTime) {
        stressInteractor.getStress(userId, startTime, endTime, new ResourceRequestCallback<List<Stress>>() {
            @Override
            public void onSucess(List<Stress> stresses) {
                updateMap(stresses);
            }

            @Override
            public void onError(BaseException e) {

            }
        });

    }

    protected void updateMap(List<Stress> stresses) {
        clearMarkers();
        addMarkers(stresses);
        moveCamera();
    }

    private void clearMarkers() {
        for (int i = 0; i < _markers.size(); i++) {
            _markers.get(i).remove();
        }
    }

    private void addMarkers(List<Stress> stresses) {
        _markers = new ArrayList<>();
        for (int i = 0; i < stresses.size(); i++) {
            LatLng position = new LatLng(stresses.get(i).getLat(), stresses.get(i).getLng());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(position).anchor(0.5f, 0.5f));
            _markers.add(marker);
        }
    }

    private void moveCamera() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : _markers) {
            builder.include(marker.getPosition());
        }



        int padding = 75; // offset from edges of the map in pixels

        if (_markers.size() > 0) {
            LatLngBounds bounds = builder.build();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        }
    }

    protected void injectDependencies() {
        App.get(getActivity()).inject(this);
    }
}
