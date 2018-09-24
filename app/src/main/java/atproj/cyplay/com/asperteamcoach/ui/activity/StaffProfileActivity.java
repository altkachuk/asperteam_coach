package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.RoleType;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamapi.util.StringHelper;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseResourceActivity;
import butterknife.BindView;

/**
 * Created by andre on 14-Apr-18.
 */

public class StaffProfileActivity extends BaseResourceActivity {

    @Inject
    ProfileInteractor profileInteractor;

    @Inject
    Picasso picasso;

    @BindView(R.id.photoImage)
    ImageView photoImage;

    @BindView(R.id.nameText)
    TextView nameText;

    @BindView(R.id.roleText)
    TextView roleText;

    @BindView(R.id.companyText)
    TextView companyText;

    @BindView(R.id.emailText)
    TextView emailText;

    @BindView(R.id.phoneText)
    TextView phoneText;

    @BindView(R.id.experienceText)
    TextView experienceText;

    @BindView(R.id.availabilityAsCoachLayout)
    RelativeLayout availabilityAsCoachLayout;

    @BindView(R.id.availabilityAsCoachText)
    TextView availabilityAsCoachText;

    @BindView(R.id.officeNumberLayout)
    RelativeLayout officeNumberLayout;

    @BindView(R.id.officeNumberText)
    TextView officeNumberText;

    private String _id;
    private RoleType _roleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _id = getIntent().getExtras().get("id").toString();
        String roleTypeExt = getIntent().getExtras().get("role_type").toString();
        _roleType = RoleType.values()[Integer.parseInt(roleTypeExt)];

        if (_roleType != RoleType.COACH_PERMANENT && _roleType != RoleType.COACH)
            availabilityAsCoachLayout.setVisibility(View.GONE);
        else
            officeNumberLayout.setVisibility(View.GONE);

        roleText.setText(StringHelper.getRoleName(_roleType, getApplicationContext()));

        clear();
    }

    @Override
    protected void load() {
        getStaff(_id);
    }

    private void clear() {
        nameText.setText("");
        companyText.setText("");
        emailText.setText("");
        phoneText.setText("");
        experienceText.setText("");
        availabilityAsCoachText.setText("");
        officeNumberText.setText("");
    }

    private void getStaff(String id) {
        showPreloader();
        profileInteractor.getStaff(id, new ResourceRequestCallback<User>() {
            @Override
            public void onSucess(User user) {
                hidePreloader();
                if (user.getImage() != null && user.getImage().length() > 0)
                    picasso.load(user.getImage()).transform(new CircleTransform(getApplicationContext(), R.color.colorImageCircleStroke)).into(photoImage);
                nameText.setText(user.getFirstName() + " " + user.getLastName());
                companyText.setText(user.getCompany());
                emailText.setText(user.getEmail());
                phoneText.setText(user.getPhone());
                experienceText.setText(user.getExperience());
                availabilityAsCoachText.setText(user.getAvailabilityAsCoach());
                officeNumberText.setText(user.getOfficeNumber());
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }
}
