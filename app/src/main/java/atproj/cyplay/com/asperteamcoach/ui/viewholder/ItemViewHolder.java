package atproj.cyplay.com.asperteamcoach.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import atproj.cyplay.com.asperteamcoach.dagger.App;
import butterknife.ButterKnife;

/**
 * Created by andre on 20-Apr-18.
 */

public abstract class ItemViewHolder<Item> extends RecyclerView.ViewHolder {

    protected Context _context;

    public ItemViewHolder(View itemView) {
        super(itemView);
        _context = itemView.getContext();
        App.get(_context).inject(this);
        ButterKnife.bind(this, itemView);
    }

    public abstract void setItem(Item item);
}
