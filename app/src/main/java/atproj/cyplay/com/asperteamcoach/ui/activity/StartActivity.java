package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.repository.AsperTeamApi;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseActivity;
import atproj.cyplay.com.asperteamcoach.ui.fragment.LoginFragment;

public class StartActivity extends BaseActivity implements LoginFragment.OnLoginListener {

    private final int TERMS_OF_CONDITION = 0;

    @Inject
    AsperTeamApi asperTeamApi;

    @Inject
    UserSettingsUtil userSettings;

    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginFragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.loginFragment);
    }

    @Override
    public void onLogin() {
        Spanned description = Html.fromHtml(
                getString(R.string.terms_of_condition_text1) +
                        getString(R.string.terms_of_condition_text2) +
                        getString(R.string.terms_of_condition_text3)
        );
        getPopup().initPopup(
                TERMS_OF_CONDITION,
                getString(R.string.terms_of_condition_title),
                description);
        getPopup().setImage(R.drawable.img_braccelet);
        getPopup().setPositive(getString(R.string.agree_button));
        getPopup().setNegative(getString(R.string.disagree_button));
        getPopup().show();
    }

    @Override
    protected void onPopupNegativeClick(int requestCode) {
        loginFragment.clear();
    }

    @Override
    protected void onPopupPositiveClick(int requestCode) {
        startHomeActivity();
    }

    private void startHomeActivity() {
        Intent homeIntent = new Intent(this.getApplicationContext(), HomeActivity.class);
        this.startActivity(homeIntent);
        this.finish();
    }
}
