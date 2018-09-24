package atproj.cyplay.com.asperteamcoach.ui.customview.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import atproj.cyplay.com.asperteamcoach.R;
import butterknife.ButterKnife;

/**
 * Created by andre on 11-May-18.
 */

public class BaseView extends RelativeLayout {

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyFragment);
        int resourceId = a.getResourceId(R.styleable.MyFragment_view_resource, -1);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(resourceId, this, true);

        injectViews(this);
    }

    protected void injectViews(View view) {
        ButterKnife.bind(this, view);
    }
}
