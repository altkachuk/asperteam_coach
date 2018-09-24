package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.SituationInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.SituationResource;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.Crossknowledge;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseResourceActivity;
import atproj.cyplay.com.asperteamcoach.ui.adapter.SituationAdapter;
import atproj.cyplay.com.asperteamcoach.ui.decorator.DividerItemDecoration;
import atproj.cyplay.com.asperteamcoach.ui.listener.RecyclerItemClickListener;
import butterknife.BindView;

/**
 * Created by andre on 23-Apr-18.
 */

public class SituationsActivity extends BaseResourceActivity {

    @Inject
    SituationInteractor situationInteractor;

    @Inject
    Crossknowledge crossknowledge;

    @BindView(R.id.situationListRecyclerView)
    RecyclerView recyclerView;

    private int _situationId;
    private SituationAdapter _adapter;
    private List<SituationResource> _resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situations);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _situationId = Integer.parseInt(getIntent().getExtras().get("situation_id").toString());

        _adapter = new SituationAdapter(getApplicationContext(), new ArrayList<SituationResource>());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupRecyclerView();
    }

    @Override
    protected void load() {
        getSitationResources(_situationId);
    }

    private void getSitationResources(int id) {
        showPreloader();
        situationInteractor.getSituationResources(id, new ResourceRequestCallback<List<SituationResource>>() {
            @Override
            public void onSucess(List<SituationResource> resources) {
                hidePreloader();
                _resources = resources;
                _adapter.setItems(_resources);
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }


    private void setupRecyclerView() {
        // optimization
        recyclerView.setHasFixedSize(true);
        // set layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(_adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider_situation));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new ResourceClick()));
    }

    private class ResourceClick implements RecyclerItemClickListener.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            openResource(_resources.get(position).getTrainingContentId());
        }
    }

    private void openResource(String trainingContentId) {
        String url = crossknowledge.buildUrl(trainingContentId);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

}
