package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseMenuActivity;
import butterknife.BindView;

/**
 * Created by andre on 18-Jun-18.
 */

public class ParametersActivity extends BaseMenuActivity {

    @BindView(R.id.versionText)
    TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        try {
            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionText.setText(pinfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            ;
        }
    }
}
