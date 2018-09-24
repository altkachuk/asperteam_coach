package atproj.cyplay.com.asperteamcoach.ui.fragment;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import atproj.cyplay.com.asperteamapi.util.FacebookMessangerUtils;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.HomeActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.ProblemCategoriesActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.RelaxActivity;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 19-Apr-18.
 */

public class BottomMenuFragment extends BaseFragment {

    @BindView(R.id.homeItem)
    ImageView homeItem;

    @BindView(R.id.relaxItem)
    ImageView relaxItem;

    @BindView(R.id.qualifySituationsItem)
    ImageView qualifySituationsItem;

    @BindView(R.id.callItem)
    ImageView callItem;

    private Class _activeActivityClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_menu, container, false);
    }

    public void setActiveActivityClass(Class activityClass) {
        _activeActivityClass = activityClass;

        changeActiveState(homeItem, HomeActivity.class, R.drawable.ic_home_activ, R.drawable.ic_home);
        changeActiveState(relaxItem, RelaxActivity.class, R.drawable.ic_relax_active, R.drawable.ic_relax);
        changeActiveState(qualifySituationsItem, ProblemCategoriesActivity.class, R.drawable.ic_problem_active, R.drawable.ic_problem);
    }

    private void changeActiveState(ImageView item, Class activeClass, int activeIcRes, int notActiveIcRes) {
        if (_activeActivityClass.equals(activeClass)) {
            item.setImageResource(activeIcRes);
            item.setEnabled(false);
        } else {
            item.setImageResource(notActiveIcRes);
            item.setEnabled(true);
        }
    }

    @OnClick(R.id.homeItem)
    public void onClickHomeItem() {
        Intent homeIntent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
        this.startActivity(homeIntent);
        getActivity().finish();
    }

    @OnClick(R.id.relaxItem)
    public void onClickRelaxItem() {
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

    @OnClick(R.id.callItem)
    public void onClickCallItem() {
        openMessageDialog();
    }

    @OnClick(R.id.communityItem)
    public void onClickCommunityItem() {
        openMessageDialog(getString(R.string.community_fb_url));
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

    private void openMessageDialog() {
        if (FacebookMessangerUtils.isInstalled(getActivity().getApplicationContext())) {
            FacebookMessangerUtils.startMessanger(getActivity());
        } else {
            getPopup().initPopup(0,
                    getString(R.string.messanger_not_installed_title),
                    getString(R.string.messanger_not_installed_description));
            getPopup().setPositive(getString(R.string.agree_button));
            getPopup().show();
        }
    }

}
