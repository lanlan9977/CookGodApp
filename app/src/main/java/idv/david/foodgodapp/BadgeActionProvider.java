package idv.david.foodgodapp;


import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BadgeActionProvider extends ActionProvider {

    private ImageView ivIcon;
    private TextView tvBadge;

    private int clickWhat;
    private OnClickListener onClickListener;

    public BadgeActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        int size = getContext().getResources().getDimensionPixelSize(android.support.design.R.dimen.abc_action_bar_default_height_material);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_badge_provider, null, false);
        view.setLayoutParams(layoutParams);
        ivIcon =view.findViewById(R.id.ivIcon);
        tvBadge = view.findViewById(R.id.tvBadge);
        view.setOnClickListener(onViewClickListener);
        return view;
    }

    private View.OnClickListener onViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onClickListener != null)
                onClickListener.onClick(clickWhat);
        }
    };

    public void setOnClickListener(int what, OnClickListener onClickListener) {
        this.clickWhat = what;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int what);
    }

    public void setIcon(int icon) {
        ivIcon.setImageResource(icon);
    }

    public void setBadge(int i) {
        if (i > 99) {
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText("99+");
        } else if (i > 0) {
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(i));
        } else {
            tvBadge.setText(null);
            tvBadge.setVisibility(View.INVISIBLE);
        }
    }

    public void setTextInt(int i) {
        tvBadge.setText(i);
    }

    public void setText(CharSequence i) {
        tvBadge.setText(i);
    }
}
