package atproj.cyplay.com.asperteamcoach.ui.fragment.base;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import atproj.cyplay.com.asperteamcoach.R;
import butterknife.BindView;

/**
 * Created by andre on 14-Apr-18.
 */

public class BaseResourceFragment extends BaseFragment {

    @Nullable
    @BindView(R.id.progressBar)
    RelativeLayout progressBar;

    public void load() {
        ;
    }

    public void showPreloader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hidePreloader() {
        progressBar.setVisibility(View.GONE);
    }
}
