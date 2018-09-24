package atproj.cyplay.com.asperteamcoach.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.RoleType;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamapi.util.StringHelper;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.StaffProfileActivity;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BaseResourceFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 05-Apr-18.
 */

public class StaffProfileItemFragment extends BaseResourceFragment {

    @Inject
    Picasso picasso;

    @Inject
    ProfileInteractor profileInteractor;

    @BindView(R.id.photoImage)
    ImageView photoImage;

    @BindView(R.id.nameText)
    TextView nameText;

    @BindView(R.id.roleText)
    TextView roleText;

    protected RoleType _roleType;
    private String _id = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_staff_profile, container, false);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyFragment);
        _roleType = RoleType.values()[a.getInt(R.styleable.MyFragment_role_type, -1)];
        a.recycle();
    }

    @Override
    @Deprecated
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.MyFragment);
        _roleType = RoleType.values()[a.getInt(R.styleable.MyFragment_role_type, -1)];
        a.recycle();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showPreloader();
        roleText.setText(StringHelper.getRoleName(_roleType, getActivity().getApplicationContext()));
    }

    @Override
    public void load() {
        if (_id != null)
            getStaff(_id);
    }

    public void setUser(User user) {
        _id = user.getStaffId(_roleType);
        load();
    }

    private void getStaff(String id) {
        showPreloader();
        profileInteractor.getStaff(id, new ResourceRequestCallback<User>() {
            @Override
            public void onSucess(User user) {
                hidePreloader();
                if (user.getImage() != null && user.getImage().length() > 0)
                    picasso.load(user.getImage()).transform(new CircleTransform(getActivity().getApplicationContext(), R.color.colorImageCircleStroke)).into(photoImage);
                nameText.setText(user.getFirstName() + " " + user.getLastName());
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }

    @OnClick(R.id.photoImage)
    protected void onPhotoImageClick() {
        openMyProfileActivity(_id, _roleType);
    }

    private void openMyProfileActivity(String id, RoleType roleType) {
        Intent staffProfileIntent = new Intent(getActivity().getApplicationContext(), StaffProfileActivity.class);
        staffProfileIntent.putExtra("id", id);
        staffProfileIntent.putExtra("role_type", roleType.ordinal());
        startActivity(staffProfileIntent);
    }

}
