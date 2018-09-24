package atproj.cyplay.com.asperteamcoach.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.customview.base.BaseResourceView;
import butterknife.BindView;

/**
 * Created by andre on 11-May-18.
 */

public class ProfileTextItemView extends BaseResourceView {

    @BindView(R.id.titleText)
    TextView titleText;

    @BindView(R.id.valueText)
    TextView valueText;

    public ProfileTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyFragment);
        int titleRes = a.getResourceId(R.styleable.MyFragment_title_res, -1);

        titleText.setText(titleRes);
    }

    public void setValue(String value) {
        valueText.setText(value);
    }
}
