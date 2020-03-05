package hn19_fr_android_05.basic_android_exam_trungpx4.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hn19_fr_android_05.basic_android_exam_trungpx4.R;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private Context mContext;

    private OnClickListener mOnClickListener;

    private final ArrayList<ApplicationInfo> mListResolveInfo = new ArrayList<>();

    public AppAdapter(OnClickListener onClickListener, ArrayList<ApplicationInfo> listResolveInfo) {
        mOnClickListener = onClickListener;
        if (listResolveInfo != null) {
            mListResolveInfo.addAll(listResolveInfo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setHolder(mContext.getPackageManager().getApplicationIcon(mListResolveInfo.get(i)),
                mContext.getPackageManager().getApplicationLabel(mListResolveInfo.get(i)));
    }

    @Override
    public int getItemCount() {
        return mListResolveInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgLauch;
        TextView mTvAppName;

        void setHolder(Drawable lauch, CharSequence name) {
            mImgLauch.setImageDrawable(lauch);
            Glide.with(mContext).load(lauch).into(mImgLauch);
            mTvAppName.setText(name);
        }

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImgLauch = itemView.findViewById(R.id.img_app);
            mTvAppName = itemView.findViewById(R.id.tv_app_name);
            View.OnClickListener mItemViewClicListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.itemAppClick(getAdapterPosition(), mListResolveInfo.get(getAdapterPosition()));
                    }
                }
            };
            itemView.setOnClickListener(mItemViewClicListener);
        }
    }

    public interface OnClickListener {
        void itemAppClick(int position, ApplicationInfo applicationInfo);
    }
}
