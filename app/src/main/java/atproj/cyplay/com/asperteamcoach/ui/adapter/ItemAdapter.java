package atproj.cyplay.com.asperteamcoach.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import atproj.cyplay.com.asperteamcoach.ui.viewholder.ItemViewHolder;

/**
 * Created by andre on 20-Apr-18.
 */
public abstract class ItemAdapter<Item, T extends ItemViewHolder<Item>> extends RecyclerView.Adapter<T> {

    protected Context _context;
    protected List<Item> _items;

    public ItemAdapter(Context context) {
        _context = context;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return _items != null ? _items.size() : 0;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        Item item = _items.get(position);
        if (item != null) {
            holder.setItem(item);
        }
    }


    public void setItems(List<Item> items) {
        _items = items;
        notifyDataSetChanged();
    }

    public void addRangeItems(List<Item> rangeItems) {
        if (_items != null)
            _items = new ArrayList<>();
        if (rangeItems != null) {
            int pos = _items.size();
            _items.addAll(rangeItems);
            notifyItemRangeInserted(pos, _items.size());
        }
    }

    public void clearItems() {
        if (_items != null && _items.size() > 0) {
            int oldsize = _items.size();
            _items.clear();
            notifyItemRangeRemoved(0, oldsize);
        }
    }

    public List<Item> getItems() {
        return _items;
    }
}
