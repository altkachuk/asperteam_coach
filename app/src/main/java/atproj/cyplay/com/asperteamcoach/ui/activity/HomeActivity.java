package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.ActivityHelper;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseMenuActivity;
import atproj.cyplay.com.asperteamcoach.ui.adapter.PatientAdapter;
import atproj.cyplay.com.asperteamcoach.ui.decorator.DividerItemDecoration;
import atproj.cyplay.com.asperteamcoach.ui.listener.RecyclerItemClickListener;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

/**
 * Created by andre on 30-Mar-18.
 */

public class HomeActivity extends BaseMenuActivity {

    @Inject
    UserSettingsUtil userSettings;

    @Inject
    ProfileInteractor profileInteractor;

    @BindView(R.id.searchView)
    EditText searchView;

    @BindView(R.id.searchButton)
    ImageView searchButton;

    @BindView(R.id.closeButton)
    ImageView closeButton;

    @BindView(R.id.patientListRecyclerView)
    RecyclerView recyclerView;

    private HashMap<String, User> _patients;
    private PatientAdapter _adapter;

    private int _counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        _adapter = new PatientAdapter(getApplicationContext(), new ArrayList<User>());
        _patients = new HashMap<>();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        recyclerView.requestFocus();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupRecyclerView();
    }

    @Override
    protected void onPostMenuCreated() {
        _counter = 0;
        for (String id : _user.getPatientIds()) {
            getPatient(id);
        }
    }

    private void getPatient(String id) {
        showPreloader();
        profileInteractor.getPatient(id, new ResourceRequestCallback<User>() {
            @Override
            public void onSucess(User user) {
                hidePreloader();
                _patients.put(user.getId(), user);
                _counter++;

                if (_counter == _user.getPatientIds().size())
                    updateAdapter();
            }

            @Override
            public void onError(BaseException e) {
                _counter++;
                if (_counter == _user.getPatientIds().size())
                    updateAdapter();
            }
        });
    }

    private void updateAdapter() {
        hidePreloader();
        List<User> patients = new ArrayList<>();
        for (String id : _user.getPatientIds()) {
            patients.add(_patients.get(id));
        }

        _adapter.setItems(patients);
    }


    private void setupRecyclerView() {
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(_adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider_problem_category));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new PatientClick()));
    }

    @OnTextChanged(value = R.id.searchView, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChangedSearchView(Editable editable) {
        _adapter.getFilter().filter(editable.toString());

        if (editable.length() > 0) {
            searchButton.setVisibility(View.GONE);
            closeButton.setVisibility(View.VISIBLE);
        } else {
            searchButton.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.closeButton)
    public void onClickCloseButton() {
        searchView.setText("");
        ActivityHelper.hideKeyboard(this);
    }

    @OnEditorAction({R.id.searchView})
    protected boolean onTextAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            ActivityHelper.hideKeyboard(this);
            return true;
        }
        return false;
    }

    private class PatientClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            openPatientActivity(_adapter.getUserByPosition(position).getId()/* _user.getPatientIds().get(position)*/);
        }
    }

    private void openPatientActivity(String id) {
        Intent staffProfileIntent = new Intent(getApplicationContext(), PatientActivity.class);
        staffProfileIntent.putExtra("id", id);
        this.startActivity(staffProfileIntent);
    }

}
