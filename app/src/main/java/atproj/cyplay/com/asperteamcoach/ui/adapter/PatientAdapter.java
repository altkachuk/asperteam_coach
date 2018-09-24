package atproj.cyplay.com.asperteamcoach.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.viewholder.PatientViewHolder;

/**
 * Created by andre on 21-Apr-18.
 */

public class PatientAdapter extends ItemAdapter<User, PatientViewHolder> implements Filterable {

    protected List<User> _patientList;
    private UserFilter _userFilter;


    public PatientAdapter(Context context, List<User> patients) {
        super(context);
        _patientList = patients;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_patient, parent, false);
        return new PatientViewHolder(view, _context);
    }

    @Override
    public Filter getFilter() {
        if (_userFilter == null)
            _userFilter = new UserFilter(this, _items);
        return _userFilter;
    }

    public User getUserByPosition(int position) {
        return _items.get(position);
    }

    public static class UserFilter extends Filter {

        private final PatientAdapter _adapter;
        private List<User> _items;
        private List<User> _filtereredItems;


        private UserFilter(PatientAdapter adapter, List<User> patients) {
            super();
            _adapter =  adapter;
            _items = new LinkedList<>(patients);
            _filtereredItems = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            _filtereredItems.clear();
            final FilterResults results = new FilterResults();

            if (charSequence.length() == 0) {
                _filtereredItems.addAll(_items);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();

                for (final User user : _items) {
                    if (user.getFirstName().toLowerCase().contains(filterPattern) || user.getLastName().toLowerCase().contains(filterPattern))
                        _filtereredItems.add(user);
                }
            }

            results.values = _filtereredItems;
            results.count = _filtereredItems.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            _adapter._items.clear();
            _adapter._items.addAll((ArrayList<User>) filterResults.values);
            _adapter.notifyDataSetChanged();
        }
    }
}
