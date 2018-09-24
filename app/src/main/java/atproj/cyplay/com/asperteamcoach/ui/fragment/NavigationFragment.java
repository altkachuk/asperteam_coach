package atproj.cyplay.com.asperteamcoach.ui.fragment;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamapi.util.FacebookMessangerUtils;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.HomeActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.ParametersActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.ProblemCategoriesActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.RelaxActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.TermsOfServiceActivity;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BaseResourceFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 17-Apr-18.
 */

public class NavigationFragment extends BaseResourceFragment {

    @Inject
    ProfileInteractor profileInteractor;

    @Inject
    UserSettingsUtil userSettings;

    @Inject
    Picasso picasso;

    @BindView(R.id.photoImage)
    ImageView photoImage;

    @BindView(R.id.nameText)
    TextView nameText;

    @BindView(R.id.homeItem)
    RelativeLayout homeItem;

    @BindView(R.id.relaxItem)
    RelativeLayout relaxItem;

    @BindView(R.id.qualifySituationsItem)
    RelativeLayout qualifySituationsItem;

    @BindView(R.id.parametersItem)
    RelativeLayout parametersItem;

    @BindView(R.id.termsOfServiceItem)
    RelativeLayout termsOfServiceItem;

    private Class _activeActivityClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        update();
        load();
    }

    public void setUser(User user) {
        picasso.load(user.getImage()).transform(new CircleTransform(getActivity().getApplicationContext(), R.color.colorImageCircleStroke)).into(photoImage);
        nameText.setText(user.getFirstName() + " " + user.getLastName());
    }

    public void setActiveActivityClass(Class activityClass) {
        _activeActivityClass = activityClass;
    }

    protected void update() {
        changeActiveState(homeItem, HomeActivity.class, R.drawable.ic_home_activ, R.drawable.ic_home);
        changeActiveState(relaxItem, RelaxActivity.class, R.drawable.ic_relax_active, R.drawable.ic_relax);
        changeActiveState(qualifySituationsItem, ProblemCategoriesActivity.class, R.drawable.ic_problem_active, R.drawable.ic_problem);
        changeActiveState(parametersItem, ParametersActivity.class, R.drawable.ic_settings_active, R.drawable.ic_settings);
        changeActiveState(termsOfServiceItem, TermsOfServiceActivity.class, R.drawable.ic_info_activ, R.drawable.ic_info);
    }

    private void changeActiveState(RelativeLayout item, Class activeClass, int activeIcRes, int notActiveIcRes) {
        ImageView itemImageView = (ImageView) item.getChildAt(0);
        TextView itemTextView = (TextView) item.getChildAt(1);

        if (_activeActivityClass.equals(activeClass)) {
            itemImageView.setImageResource(activeIcRes);
            itemTextView.setTextColor(Color.parseColor("#4e7b9f"));
            item.setEnabled(false);
            item.setBackgroundResource(R.drawable.background_menu_selected_item);
        } else {
            itemImageView.setImageResource(notActiveIcRes);
            itemTextView.setTextColor(Color.parseColor("#5a5a5a"));
            item.setEnabled(true);
            item.setBackgroundResource(R.drawable.background_menu_item);
        }
    }

    @OnClick(R.id.photoImage)
    public void onClickProfile() {
        /*Intent profileIntent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
        this.startActivity(profileIntent);
        getActivity().finish();*/
    }

    @OnClick(R.id.homeItem)
    public void onClickHomeItem() {
        Intent homeIntent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
        this.startActivity(homeIntent);
        getActivity().finish();
    }

    @OnClick(R.id.relaxItem)
    public void onClickRelaxtem() {
        Intent relaxIntent = new Intent(getActivity().getApplicationContext(), RelaxActivity.class);
        TaskStackBuilder.create(getActivity())
                .addParentStack(RelaxActivity.class)
                .addNextIntent(relaxIntent)
                .startActivities();
    }

    @OnClick(R.id.qualifySituationsItem)
    public void onClickQualifySituationaItem() {
        Intent problemCategoriesIntent = new Intent(getActivity().getApplicationContext(), ProblemCategoriesActivity.class);
        TaskStackBuilder.create(getActivity())
                .addParentStack(ProblemCategoriesActivity.class)
                .addNextIntent(problemCategoriesIntent)
                .startActivities();
    }

    @OnClick(R.id.parametersItem)
    public void onClickParametersItem() {
        Intent parametersIntent = new Intent(getActivity().getApplicationContext(), ParametersActivity.class);
        TaskStackBuilder.create(getActivity())
                .addParentStack(ParametersActivity.class)
                .addNextIntent(parametersIntent)
                .startActivities();
    }

    @OnClick(R.id.termsOfServiceItem)
    public void onClickTermsOfServicetem() {
        Intent termsOfServiceIntent = new Intent(getActivity().getApplicationContext(), TermsOfServiceActivity.class);
        TaskStackBuilder.create(getActivity())
                .addParentStack(TermsOfServiceActivity.class)
                .addNextIntent(termsOfServiceIntent)
                .startActivities();
    }

    @OnClick(R.id.communityItem)
    public void onClickCommunityItem() {
        openMessageDialog(getString(R.string.community_fb_url));
    }

    @OnClick(R.id.signoutItem)
    public void onClickSignoutItem() {
        getActivity().finishAndRemoveTask();
    }

    private void openMessageDialog(String fbId) {
        if (FacebookMessangerUtils.isInstalled(getActivity().getApplicationContext())) {
            FacebookMessangerUtils.startMessanger(getActivity(), fbId);
        } else {
            getPopup().initPopup(0,
                    getString(R.string.messanger_not_installed_title),
                    getString(R.string.messanger_not_installed_description));
            getPopup().setPositive(getString(R.string.agree_button));
            getPopup().show();
        }
    }

}
