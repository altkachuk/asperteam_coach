package atproj.cyplay.com.asperteamcoach.ui.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;


import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.ActivityHelper;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.fragment.NavigationFragment;
import butterknife.BindView;

/**
 * Created by andre on 17-Apr-18.
 */

public class BaseMenuActivity extends BaseActivity {

    public final static int DELETE_ACCOUNT_REQUEST = 666;

    @Inject
    ProfileInteractor profileInteractor;

    @Inject
    UserSettingsUtil userSettings;

    // Navigation drawer:
    @Nullable
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    NavigationFragment navigationFragment;

    protected User _user;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        navigationFragment = new NavigationFragment();
        getFragmentManager().beginTransaction().replace(R.id.drawer_layout, navigationFragment).commit();
        navigationFragment.setActiveActivityClass(this.getClass());

        getUser();
    }

    private void getUser() {
        showPreloader();
        profileInteractor.getStaff(userSettings.getId(), new ResourceRequestCallback<User>() {
            @Override
            public void onSucess(User user) {
                hidePreloader();
                _user = user;
                navigationFragment.setUser(_user);
                onPostMenuCreated();
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }

    protected void onPostMenuCreated() {
        ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityHelper.hideKeyboard(this);
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
        else
            super.onBackPressed();
    }

    public void closeMenu() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onPopupPositiveClick(int requestCode) {
        switch (requestCode) {
            case DELETE_ACCOUNT_REQUEST:
                break;
        }
    }
}
