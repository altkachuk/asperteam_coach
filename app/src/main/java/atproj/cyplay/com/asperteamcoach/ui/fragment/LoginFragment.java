package atproj.cyplay.com.asperteamcoach.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.LoginInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.Login;
import atproj.cyplay.com.asperteamapi.model.OAuth2Credentials;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.ActivityHelper;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BaseResourceFragment;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

/**
 * Created by andre on 28-Mar-18.
 */

public class LoginFragment extends BaseResourceFragment {

    @Inject
    LoginInteractor loginInteractor;

    @Inject
    UserSettingsUtil userSettings;

    @BindView(R.id.usernameText)
    EditText usernameText;

    @BindView(R.id.passwordText)
    EditText passwordText;

    @BindView(R.id.signInButton)
    TextView signInButton;

    private OnLoginListener _callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInButton.setEnabled(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            _callback = (OnLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void clear() {
        userSettings.setUsername(null);
        userSettings.setPassword(null);
        userSettings.clearAuthCredentials();

        usernameText.setText("");
        passwordText.setText("");
    }

    @OnTextChanged(value = R.id.usernameText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChangedEmailText(Editable editable) {
        boolean signInEnable = usernameText.getText().toString().length() > 0 && passwordText.getText().toString().length() > 0;
        signInButton.setEnabled(signInEnable);
    }

    @OnTextChanged(value = R.id.passwordText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChangedPasswordText(Editable editable) {
        boolean signInEnable = usernameText.getText().toString().length() > 0 && passwordText.getText().toString().length() > 0;
        signInButton.setEnabled(signInEnable);
    }

    @OnEditorAction({R.id.usernameText, R.id.passwordText})
    protected boolean onTextAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            ActivityHelper.hideKeyboard(getActivity());
            boolean isValid = usernameText.getText().toString().length() > 0 && passwordText.getText().toString().length() > 0;
            if (isValid) {
                doLogin(usernameText.getEditableText().toString(), passwordText.getEditableText().toString());
            }
            return true;
        }
        return false;
    }

    @OnClick(R.id.signInButton)
    public void onClickSignInButton() {
        ActivityHelper.hideKeyboard(getActivity());
        doLogin(usernameText.getEditableText().toString(), passwordText.getEditableText().toString());
    }

    private void doLogin(final String username, final String password) {
        String token = FirebaseInstanceId.getInstance().getToken();
        Login login = new Login(username, password, token);

        setEnabledContent(false);
        showPreloader();
        loginInteractor.authenticate(login, new ResourceRequestCallback<OAuth2Credentials>() {
            @Override
            public void onSucess(OAuth2Credentials oAuth2Credentials) {
                hidePreloader();
                setEnabledContent(true);
                userSettings.setUsername(username);
                userSettings.setPassword(password);
                userSettings.setAuthCredentials(oAuth2Credentials);
                _callback.onLogin();
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
                setEnabledContent(true);
                switch (e.getExceptionType()) {
                    case AUTHENTICATION:
                        getPopup().initPopup(-1, getString(R.string.authentication_error_title), getString(R.string.authentication_error_description));
                        getPopup().setNegative(getString(R.string.ok_button));
                        getPopup().show();
                        break;
                    case TECHNICAL:
                        getPopup().initPopup(-1, getString(R.string.technical_error_title), getString(R.string.technical_error_description));
                        getPopup().setPositive(getString(R.string.ok_button));
                        getPopup().show();
                        break;
                }
            }
        });
    }

    private void setEnabledContent(Boolean enabled) {
        usernameText.setEnabled(enabled);
        passwordText.setEnabled(enabled);
        signInButton.setEnabled(enabled);
    }


    public interface OnLoginListener {
        void onLogin();
    }
}
