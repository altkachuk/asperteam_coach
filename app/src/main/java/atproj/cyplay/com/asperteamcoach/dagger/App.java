package atproj.cyplay.com.asperteamcoach.dagger;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

import atproj.cyplay.com.asperteamapi.dagger.component.AdminNetComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.AdminOkHttpClientComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.ProfileAdminInteractorComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.ApplicationComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.CrossknowledgeComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.InteractorComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.NetComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.OkHttpClientComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.PicassoComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.UserSettingsComponent;

import atproj.cyplay.com.asperteamapi.dagger.component.DaggerAdminNetComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerAdminOkHttpClientComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerProfileAdminInteractorComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerApplicationComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerCrossknowledgeComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerInteractorComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerNetComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerOkHttpClientComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerPicassoComponent;
import atproj.cyplay.com.asperteamapi.dagger.component.DaggerUserSettingsComponent;

import atproj.cyplay.com.asperteamapi.dagger.module.AdminNetModule;
import atproj.cyplay.com.asperteamapi.dagger.module.AdminOkHttpClientModule;
import atproj.cyplay.com.asperteamapi.dagger.module.ApplicationModule;
import atproj.cyplay.com.asperteamapi.dagger.module.CrossknowledgeModule;
import atproj.cyplay.com.asperteamapi.dagger.module.InteractorModule;
import atproj.cyplay.com.asperteamapi.dagger.module.NetModule;
import atproj.cyplay.com.asperteamapi.dagger.module.OkHttpClientModule;
import atproj.cyplay.com.asperteamapi.dagger.module.PicassoModule;
import atproj.cyplay.com.asperteamapi.dagger.module.ProfileAdminInteractorModule;
import atproj.cyplay.com.asperteamapi.dagger.module.UserSttingsModule;
import atproj.cyplay.com.asperteamapi.util.ClientUtil;
import atproj.cyplay.com.asperteamcoach.BuildConfig;

/**
 * Created by andre on 06-Jun-18.
 */

public class App extends Application {

    ApplicationComponent applicationComponent;
    UserSettingsComponent userSettingsComponent;
    OkHttpClientComponent okHttpClientComponent;
    AdminOkHttpClientComponent adminOkHttpClientComponent;
    PicassoComponent picassoComponent;
    NetComponent netComponent;
    AdminNetComponent adminNetComponent;
    InteractorComponent interactorComponent;
    ProfileAdminInteractorComponent profileAdminInteractorComponent;
    CrossknowledgeComponent crossknowledgeComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ClientUtil.setClientUrl(BuildConfig.BUILD_TYPE, BuildConfig.HOST);

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        userSettingsComponent = DaggerUserSettingsComponent.builder()
                .applicationComponent(applicationComponent)
                .userSttingsModule(new UserSttingsModule())
                .build();

        adminOkHttpClientComponent = DaggerAdminOkHttpClientComponent.builder()
                .applicationComponent(applicationComponent)
                .adminOkHttpClientModule(new AdminOkHttpClientModule(ClientUtil.getClientUrl()))
                .build();

        okHttpClientComponent = DaggerOkHttpClientComponent.builder()
                .applicationComponent(applicationComponent)
                .userSettingsComponent(userSettingsComponent)
                .okHttpClientModule(new OkHttpClientModule(ClientUtil.getClientUrl()))
                .build();

        picassoComponent = DaggerPicassoComponent.builder()
                .applicationComponent(applicationComponent)
                .okHttpClientComponent(okHttpClientComponent)
                .picassoModule(new PicassoModule())
                .build();

        netComponent = DaggerNetComponent.builder()
                .okHttpClientComponent(okHttpClientComponent)
                .netModule(new NetModule(ClientUtil.getClientUrl()))
                .build();

        adminNetComponent = DaggerAdminNetComponent.builder()
                .adminOkHttpClientComponent(adminOkHttpClientComponent)
                .adminNetModule(new AdminNetModule(ClientUtil.getClientUrl()))
                .build();

        interactorComponent = DaggerInteractorComponent.builder()
                .netComponent(netComponent)
                .interactorModule(new InteractorModule())
                .build();

        profileAdminInteractorComponent = DaggerProfileAdminInteractorComponent.builder()
                .adminNetComponent(adminNetComponent)
                .profileAdminInteractorModule(new ProfileAdminInteractorModule())
                .build();

        crossknowledgeComponent = DaggerCrossknowledgeComponent.builder()
                .applicationComponent(applicationComponent)
                .userSettingsComponent(userSettingsComponent)
                .crossknowledgeModule(new CrossknowledgeModule())
                .build();
    }

    public void inject(Object object) {
        AsperTeamCoachComponent asperTeamComponent = DaggerAsperTeamCoachComponent.builder()
                .netComponent(netComponent)
                .userSettingsComponent(userSettingsComponent)
                .picassoComponent(picassoComponent)
                .interactorComponent(interactorComponent)
                .profileAdminInteractorComponent(profileAdminInteractorComponent)
                .crossknowledgeComponent(crossknowledgeComponent)
                .build();

        try {
            Method method = asperTeamComponent.getClass().getMethod("inject", object.getClass());
            method.invoke(asperTeamComponent, object);
        } catch (Exception e) {
        }
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
}
