package atproj.cyplay.com.asperteamcoach.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import atproj.cyplay.com.asperteamapi.model.SituationResource;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.viewholder.SituationViewHolder;

/**
 * Created by andre on 21-Apr-18.
 */

public class SituationAdapter extends ItemAdapter<SituationResource, SituationViewHolder> {


    public SituationAdapter(Context context, List<SituationResource> resources) {
        super(context);
        _items = resources;
    }

    @Override
    public SituationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_situation, parent, false);
        return new SituationViewHolder(view);
    }
}
