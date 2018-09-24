package atproj.cyplay.com.asperteamcoach.dagger;

import atproj.cyplay.com.asperteamapi.dagger.component.CrossknowledgeComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.InteractorComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.NetComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.PicassoComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.ProfileAdminInteractorComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.UserSettingsComponent;
import atproj.cyplay.com.asperteamapi.dagger.scope.RuntimeScope;
import atproj.cyplay.com.asperteamcoach.ui.activity.HistoryActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.HomeActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.ParametersActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.PatientActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.PatientProfileActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.ProblemCategoriesActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.ProfileActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.RelaxActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.SituationsActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.SplashActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.StaffProfileActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.StartActivity;
import atproj.cyplay.com.asperteamcoach.ui.activity.TermsOfServiceActivity;
import atproj.cyplay.com.asperteamcoach.ui.customview.StressHistoryFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.LoginFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.MyProfileItemFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.NavigationFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.RmssdHistoryFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.StaffProfileItemFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BasePopupFragment;
import atproj.cyplay.com.asperteamcoach.ui.viewholder.PatientViewHolder;
import atproj.cyplay.com.asperteamcoach.ui.viewholder.SituationViewHolder;
import dagger.Component;

/**
 * Created by andre on 06-Jun-18.
 */

@RuntimeScope
@Component(dependencies = {
        NetComponent.class,
        UserSettingsComponent.class,
        PicassoComponent.class,
        InteractorComponent.class,
        ProfileAdminInteractorComponent.class,
        CrossknowledgeComponent.class})

public interface AsperTeamCoachComponent {

    // activities
    void inject(SplashActivity splashActivity);
    void inject(StartActivity startActivity);
    void inject(HomeActivity homeActivity);
    void inject(PatientActivity patientActivity);
    void inject(HistoryActivity historyDayActivity);
    void inject(ProfileActivity profileActivity);
    void inject(PatientProfileActivity patientProfileActivity);
    void inject(StaffProfileActivity staffProfileActivity);
    void inject(ProblemCategoriesActivity problemCategoriesActivity);
    void inject(SituationsActivity situationsActivity);
    void inject(RelaxActivity relaxActivity);
    void inject(ParametersActivity parametersActivity);
    void inject(TermsOfServiceActivity termsOfServiceActivity);

    // fragments
    void inject(BasePopupFragment basePopupFragment);
    void inject(LoginFragment loginFragment);
    void inject(NavigationFragment navigationFragment);
    void inject(RmssdHistoryFragment rmssdHistoryFragment);
    void inject(StressHistoryFragment stressHistoryFragment);
    void inject(MyProfileItemFragment myProfileItemFragment);
    void inject(StaffProfileItemFragment staffProfileItemFragment);

    // View Holder
    void inject(PatientViewHolder patientViewHolder);
    void inject(SituationViewHolder situationViewHolder);
}
