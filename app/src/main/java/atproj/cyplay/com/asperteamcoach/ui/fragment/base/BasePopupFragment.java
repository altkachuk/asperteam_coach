package atproj.cyplay.com.asperteamcoach.ui.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import atproj.cyplay.com.asperteamcoach.R;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 30-Mar-18.
 */

public class BasePopupFragment extends BaseFragment {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.titleText)
    TextView titleText;

    @BindView(R.id.descriptionText)
    TextView descriptionText;

    @BindView(R.id.positiveButton)
    Button positiveButton;

    @BindView(R.id.negativeButton)
    Button negativeButton;

    OnPositiveClickListener _positiveCallback;
    OnNegativeClickListener _negativeCallback;

    private int _layoutPopupRes;
    private int _requestCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(_layoutPopupRes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyFragment);
        _layoutPopupRes = a.getResourceId(R.styleable.MyFragment_layout_popup, -1);
    }

    @Override
    @Deprecated
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.getApplication().obtainStyledAttributes(attrs, R.styleable.MyFragment);
        _layoutPopupRes = a.getResourceId(R.styleable.MyFragment_layout_popup, -1);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void initPopup(int requestCode, String title) {
        _requestCode = requestCode;
        titleText.setText(title);

        positiveButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
    }

    public void initPopup(int requestCode, String title, String description) {
        _requestCode = requestCode;
        titleText.setText(title);
        descriptionText.setText(description);

        positiveButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
    }

    public void initPopup(int requestCode, String title, Spanned description) {
        _requestCode = requestCode;
        titleText.setText(title);
        descriptionText.setText(description);

        positiveButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
    }

    public void setPositive(String positiveText) {
        positiveButton.setText(positiveText);
        positiveButton.setVisibility(View.VISIBLE);
    }

    public void setNegative(String negativeText) {
        negativeButton.setText(negativeText);
        negativeButton.setVisibility(View.VISIBLE);
    }

    public void setImage(int imageRes) {
        imageView.setImageResource(imageRes);
        imageView.setVisibility(View.VISIBLE);
    }

    public void setOnPositiveClickListener(OnPositiveClickListener l) {
        _positiveCallback = l;
    }

    public void setOnNegativeClickListener(OnNegativeClickListener l) {
        _negativeCallback = l;
    }

    @OnClick(R.id.positiveButton)
    public void onClickAgreeButton() {
        if (_positiveCallback != null)
            _positiveCallback.onClick(_requestCode);
    }

    @OnClick(R.id.negativeButton)
    public void onClickDisagreeButton() {
        if (_negativeCallback != null)
            _negativeCallback.onClick(_requestCode);
    }


    public interface OnPositiveClickListener {
        void onClick(int requestCode);
    }

    public interface OnNegativeClickListener {
        void onClick(int requestCode);
    }

}
