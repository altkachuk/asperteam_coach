package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.LoginInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.Login;
import atproj.cyplay.com.asperteamapi.model.OAuth2Credentials;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseActivity;

/**
 * Created by andre on 30-Mar-18.
 */

public class SplashActivity extends BaseActivity {

    /*@Inject
    AsperTeamApi asperTeamApi;*/

    @Inject
    LoginInteractor loginInteractor;

    @Inject
    UserSettingsUtil userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (userSettings.getToken() != null && userSettings.getId() != null) {
            startHomeActivity();
        } else if (userSettings.getUsername() != null && userSettings.getPassword() != null) {
            doLogin(userSettings.getUsername(), userSettings.getPassword());
        } else {
            startStartActivity();
        }
    }

    private void doLogin(String username, String password) {
        Login login = new Login(username, password);
        final SplashActivity activity = this;
        loginInteractor.authenticate(login, new ResourceRequestCallback<OAuth2Credentials>() {
            @Override
            public void onSucess(OAuth2Credentials oAuth2Credentials) {
                userSettings.setAuthCredentials(oAuth2Credentials);
                startHomeActivity();
            }

            @Override
            public void onError(BaseException e) {

            }
        });
    }

    private void startStartActivity() {
        Intent startIntent = new Intent(this.getApplicationContext(), StartActivity.class);
        this.startActivity(startIntent);
        this.finish();
    }

    private void startHomeActivity() {
        Intent homeIntent = new Intent(this.getApplicationContext(), HomeActivity.class);
        this.startActivity(homeIntent);
        this.finish();
    }


    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("AsperTeam", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("AsperTeam", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("AsperTeam", "printHashKey()", e);
        }
    }
}
