package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.FacebookProfile;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseResourceActivity;
import atproj.cyplay.com.asperteamcoach.ui.fragment.MyProfileItemFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.StaffProfileItemFragment;

/**
 * Created by andre on 05-Apr-18.
 */

public class ProfileActivity extends BaseResourceActivity {

    public static final int ADD_FACEBOOK_ACCOUNT = 0;

    @Inject
    ProfileInteractor profileInteractor;

    MyProfileItemFragment myProfile;
    StaffProfileItemFragment coachProfile;
    StaffProfileItemFragment permanentCoachProfile;
    StaffProfileItemFragment profileManagerProfile;
    StaffProfileItemFragment hrManagerProfile;
    StaffProfileItemFragment tutorProfile;
    StaffProfileItemFragment disabilityManagerProfile;

    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _id = getIntent().getExtras().get("id").toString();

        myProfile = (MyProfileItemFragment) getFragmentManager().findFragmentById(R.id.myProfile);
        coachProfile = (StaffProfileItemFragment) getFragmentManager().findFragmentById(R.id.coachProfile);
        permanentCoachProfile = (StaffProfileItemFragment) getFragmentManager().findFragmentById(R.id.permanentCoachProfile);
        profileManagerProfile = (StaffProfileItemFragment) getFragmentManager().findFragmentById(R.id.profileManagerProfile);
        hrManagerProfile = (StaffProfileItemFragment) getFragmentManager().findFragmentById(R.id.hrManagerProfile);
        tutorProfile = (StaffProfileItemFragment) getFragmentManager().findFragmentById(R.id.tutorProfile);
        disabilityManagerProfile = (StaffProfileItemFragment) getFragmentManager().findFragmentById(R.id.disabilityManagerProfile);
    }

    @Override
    protected void load() {
        getPatient(_id);
    }

    private void getPatient(String id) {
        showPreloader();
        profileInteractor.getPatient(id, new ResourceRequestCallback<User>() {
            @Override
            public void onSucess(User user) {
                hidePreloader();
                myProfile.setUser(user);
                coachProfile.setUser(user);
                permanentCoachProfile.setUser(user);
                profileManagerProfile.setUser(user);
                hrManagerProfile.setUser(user);
                tutorProfile.setUser(user);
                disabilityManagerProfile.setUser(user);
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }

}
