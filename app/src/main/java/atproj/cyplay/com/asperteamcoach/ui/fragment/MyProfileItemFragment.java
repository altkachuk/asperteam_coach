package atproj.cyplay.com.asperteamcoach.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.PatientActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.PatientProfileActivity;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BaseResourceFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 05-Apr-18.
 */

public class MyProfileItemFragment extends BaseResourceFragment {

    @Inject
    Picasso picasso;

    @Inject
    UserSettingsUtil userSettings;

    @Inject
    ProfileInteractor profileInteractor;

    @BindView(R.id.photoImage)
    ImageView photoImage;

    private User _user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_my_profile, container, false);
    }

    public void setUser(User user) {
        _user = user;
        picasso.load(user.getImage()).transform(new CircleTransform(getActivity().getApplicationContext(), R.color.colorImageCircleStroke)).into(photoImage);
    }

    @OnClick(R.id.photoImage)
    protected void onPhotoImageClick() {
        openPatientActivity(_user.getId());
    }

    private void openPatientActivity(String id) {
        Intent patientProfileIntent = new Intent(getActivity().getApplicationContext(), PatientProfileActivity.class);
        patientProfileIntent.putExtra("id", id);
        this.startActivity(patientProfileIntent);
    }
}
