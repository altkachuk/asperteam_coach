package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamapi.util.FacebookMessangerUtils;
import atproj.cyplay.com.asperteamapi.util.PhoneUtils;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseResourceActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 25-Jun-18.
 */

public class PatientActivity extends BaseResourceActivity {

    @Inject
    ProfileInteractor profileInteractor;

    @Inject
    Picasso picasso;

    @BindView(R.id.nameText)
    TextView nameText;

    @BindView(R.id.photoImage)
    ImageView photoImage;

    private String _id;
    private User _patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _id = getIntent().getExtras().get("id").toString();

        clear();
    }

    private void clear() {
        ;
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

                _patient = user;
                nameText.setText(user.getFirstName() + " " + user.getLastName());
                picasso.load(user.getImage()).transform(new CircleTransform(getApplicationContext(), R.color.colorImageCircleStroke)).into(photoImage);
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }

    @OnClick(R.id.dayHistoryItem)
    public void onClickDayHistoryItem() {
        Intent historyDayIntent = new Intent(getApplicationContext(), HistoryActivity.class);
        historyDayIntent.putExtra("id", _id);
        historyDayIntent.putExtra("stress_level_max", _patient.getPatient().getStressLevelMax());
        historyDayIntent.putExtra("period", HistoryActivity.DAY_PERIOD);
        this.startActivity(historyDayIntent);
    }

    @OnClick(R.id.weekHistoryItem)
    public void onClickWeekHistoryItem() {
        Intent historyWeekIntent = new Intent(getApplicationContext(), HistoryActivity.class);
        historyWeekIntent.putExtra("id", _id);
        historyWeekIntent.putExtra("stress_level_max", _patient.getPatient().getStressLevelMax());
        historyWeekIntent.putExtra("period", HistoryActivity.WEEK_PERIOD);
        this.startActivity(historyWeekIntent);
    }

    @OnClick(R.id.monthHistoryItem)
    public void onClickMonthHistoryItem() {
        Intent historyMonthIntent = new Intent(getApplicationContext(), HistoryActivity.class);
        historyMonthIntent.putExtra("id", _id);
        historyMonthIntent.putExtra("stress_level_max", _patient.getPatient().getStressLevelMax());
        historyMonthIntent.putExtra("period", HistoryActivity.MONTH_PERIOD);
        this.startActivity(historyMonthIntent);
    }

    @OnClick({R.id.nameText, R.id.photoImage})
    public void onClickProfile() {
        Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        profileIntent.putExtra("id", _id);
        this.startActivity(profileIntent);
    }

    @OnClick(R.id.callButton)
    public void onClickCallButton() {
        startDialActivity(_patient.getPhone());
    }

    @OnClick(R.id.chatButton)
    public void onClickChatButton() {
        if (!FacebookMessangerUtils.isInstalled(getApplicationContext())) {
            getPopup().initPopup(0,
                    getString(R.string.messanger_not_installed_title),
                    getString(R.string.messanger_not_installed_description));
            getPopup().setPositive(getString(R.string.agree_button));
            getPopup().show();
        } else if (_patient.getFbId() == null) {
            getPopup().initPopup(0,
                    getString(R.string.fb_account_empty_title),
                    getString(R.string.fb_account_empty_description));
            getPopup().setPositive(getString(R.string.agree_button));
            getPopup().show();
        } else {
            openMessageDialog(_patient.getFbId());
        }
    }

    private void openMessageDialog(String fbId) {
        FacebookMessangerUtils.startMessanger(this, fbId);
    }

    private void startDialActivity(final String phoneNumber) {
        if (phoneNumber == null) {
            getPopup().initPopup(0, "Phone number is not valid");
            getPopup().setPositive("OK");
            getPopup().show();
        } else {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", PhoneUtils.validate(phoneNumber), null)));
        }
    }
}
