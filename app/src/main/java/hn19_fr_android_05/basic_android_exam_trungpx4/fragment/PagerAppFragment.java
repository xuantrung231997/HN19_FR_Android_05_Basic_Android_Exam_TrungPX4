package hn19_fr_android_05.basic_android_exam_trungpx4.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hn19_fr_android_05.basic_android_exam_trungpx4.R;
import hn19_fr_android_05.basic_android_exam_trungpx4.adapter.AppAdapter;


public class PagerAppFragment extends Fragment {
    private static final String LIST_APP = "listApp";

    private ArrayList<ApplicationInfo> mListApplicationInfo;

    private OnFragmentListener mOnFragmentListener;

    private final AppAdapter.OnClickListener mOnClickListener = new AppAdapter.OnClickListener() {
        @Override
        public void itemAppClick(int position, ApplicationInfo applicationInfo) {
            if (mOnFragmentListener != null) {
                mOnFragmentListener.itemAppClick(position, applicationInfo);
            }
        }
    };

    public PagerAppFragment() {

    }

    public PagerAppFragment getInstance(ArrayList<ApplicationInfo> listApplicationInfo) {
        PagerAppFragment fragment = new PagerAppFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_APP, listApplicationInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mOnFragmentListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListApp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, container, false);
        initView(view);
        return view;
    }

    private void getListApp() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mListApplicationInfo = (ArrayList<ApplicationInfo>) bundle.getSerializable("listApp");
        }
    }

    private void initView(View view) {
        RecyclerView rvApp = view.findViewById(R.id.rv_app);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4,
                GridLayoutManager.HORIZONTAL, false);
        rvApp.setLayoutManager(gridLayoutManager);
        AppAdapter appAdapter = new AppAdapter(mOnClickListener, mListApplicationInfo);
        rvApp.setAdapter(appAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFragmentListener = null;
    }

    public interface OnFragmentListener {
        void itemAppClick(int position, ApplicationInfo applicationInfo);
    }
}
