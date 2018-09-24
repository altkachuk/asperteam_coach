package atproj.cyplay.com.asperteamcoach.ui.customview.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import atproj.cyplay.com.asperteamcoach.R;
import butterknife.BindView;

/**
 * Created by andre on 11-May-18.
 */

public class BaseResourceView extends BaseView {

    @Nullable
    @BindView(R.id.progressBar)
    RelativeLayout progressBar;

    public BaseResourceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showPreloader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hidePreloader() {
        progressBar.setVisibility(View.GONE);
    }


}
