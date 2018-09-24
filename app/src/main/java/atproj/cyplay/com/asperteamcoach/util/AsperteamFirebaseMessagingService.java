package atproj.cyplay.com.asperteamcoach.util;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseActivity;

/**
 * Created by andre on 12-Jul-18.
 */

public class AsperteamFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        BaseActivity.getCurrentActivity().showNotificationMessage(
                remoteMessage.getData().get("id"),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());
    }
}
