package atproj.cyplay.com.asperteamcoach.ui.viewholder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.ProfileInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.Situation;
import atproj.cyplay.com.asperteamapi.model.User;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.picasso.CircleTransform;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.viewholder.ItemViewHolder;
import butterknife.BindView;


/**
 * Created by andre on 21-Apr-18.
 */

public class PatientViewHolder extends ItemViewHolder<User> {

    @Inject
    ProfileInteractor profileInteractor;

    @Nullable
    @BindView(R.id.progressBar)
    RelativeLayout progressBar;

    @Nullable
    @BindView(R.id.nameText)
    TextView nameText;

    @Nullable
    @BindView(R.id.photoImage)
    ImageView photoImage;

    @Inject
    Picasso picasso;

    private Context _context;

    public PatientViewHolder(View view, Context context) {
        super(view);
        _context = context;
    }

    @Override
    public void setItem(User user) {
        nameText.setText(user.getFirstName() + " " + user.getLastName());
        picasso.load(user.getImage()).transform(new CircleTransform(_context, R.color.colorImageCircleStroke)).into(photoImage);
        progressBar.setVisibility(View.GONE);
    }

}
