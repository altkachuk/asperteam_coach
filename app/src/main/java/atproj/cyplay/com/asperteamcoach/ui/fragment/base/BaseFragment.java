package atproj.cyplay.com.asperteamcoach.ui.fragment.base;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import atproj.cyplay.com.asperteamcoach.dagger.App;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseActivity;
import butterknife.ButterKnife;

/**
 * Created by andre on 28-Mar-18.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    public void show() {
        this.getView().setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
    }

    public void hide() {
        this.getView().setVisibility(View.GONE);
    }

    protected BasePopupFragment getPopup() {
        return ((BaseActivity) this.getActivity()).getPopup();
    }

    public void hide(int start, int stop) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(start, stop);
    }

    protected void injectDependencies() {
        App.get(getActivity()).inject(this);
    }

    protected void injectViews(View view) {
        ButterKnife.bind(this, view);
    }
}
