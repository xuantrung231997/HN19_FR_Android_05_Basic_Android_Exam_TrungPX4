package hn19_fr_android_05.basic_android_exam_trungpx4.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Comparator;

import hn19_fr_android_05.basic_android_exam_trungpx4.R;
import hn19_fr_android_05.basic_android_exam_trungpx4.adapter.PagerAppAdapter;
import hn19_fr_android_05.basic_android_exam_trungpx4.fragment.PagerAppFragment;


public class MainActivity extends AppCompatActivity implements PagerAppFragment.OnFragmentListener {

    private LinearLayout mProgress;

    private ViewPager mViewPager;

    private Button mBtnPreviousPager;

    private Button mBtnNextPager;

    private ArrayList<ApplicationInfo> mListApplicationInfo;

    private PagerAppAdapter mPagerAppAdapter;

    private int mPagerSelectPosition;

    private int mViewPagerSize = 0;

    @Override
    public void itemAppClick(int position, ApplicationInfo applicationInfo) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private final View.OnClickListener mBtnPreviousPagerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPagerSelectPosition - 1 >= 0) {
                mPagerSelectPosition = mPagerSelectPosition - 1;
            } else {
                mPagerSelectPosition = 0;
            }
            selectPager(mPagerSelectPosition);
        }
    };

    private final View.OnClickListener mBtnNextPagerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPagerSelectPosition + 1 < mViewPagerSize) {
                mPagerSelectPosition = mPagerSelectPosition + 1;
            } else {
                mPagerSelectPosition = mViewPagerSize - 1;
            }
            selectPager(mPagerSelectPosition);
        }
    };

    private final ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            mPagerSelectPosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getListResolveInfo();
        initView();
        initAction();
        initViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_sort, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_by_name:
                showProgress();
                sortByName();

                return true;
            case R.id.sort_by_time:
                showProgress();
                sortByTime();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initAction() {
        mViewPager.addOnPageChangeListener(mPagerChangeListener);
        mBtnPreviousPager.setOnClickListener(mBtnPreviousPagerClick);
        mBtnNextPager.setOnClickListener(mBtnNextPagerClick);
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp_app);
        mBtnNextPager = findViewById(R.id.btn_viewpager_next);
        mBtnPreviousPager = findViewById(R.id.btn_viewpager_previous);
        mProgress = findViewById(R.id.progress);
    }

    private void initViewPager() {
        mPagerAppAdapter = setDataViewPager();
        mViewPager.setAdapter(mPagerAppAdapter);

    }

    private void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgress.setVisibility(View.INVISIBLE);
    }

    private PagerAppAdapter setDataViewPager() {
        mPagerAppAdapter = new PagerAppAdapter(getSupportFragmentManager());
        PagerAppFragment pagerAppFragment = new PagerAppFragment();
        ArrayList<ApplicationInfo> list = new ArrayList<>();
        for (int i = 0; i < mListApplicationInfo.size(); i++) {
            list.add(mListApplicationInfo.get(i));
            if (i != 0 && i % 16 == 0) {
                mPagerAppAdapter.addFragment(pagerAppFragment.getInstance(list));
                list = new ArrayList<>();
            }
        }
        mViewPagerSize = mPagerAppAdapter.getCount();
        return mPagerAppAdapter;
    }

    private void getListResolveInfo() {
        final PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mListApplicationInfo = (ArrayList<ApplicationInfo>) getPackageManager().getInstalledApplications(PackageManager.MATCH_SYSTEM_ONLY);
        }
        for (int i = 0; i < mListApplicationInfo.size(); i++) {
            if (pm.getLaunchIntentForPackage(mListApplicationInfo.get(i).packageName) != null &&

                    !pm.getLaunchIntentForPackage(mListApplicationInfo.get(i).packageName).equals("")) {
                mListApplicationInfo.remove(i);
            }
        }
    }

    private void selectPager(int select) {
        mViewPager.setCurrentItem(select);
    }

    private void sortByTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mListApplicationInfo.sort(new Comparator<ApplicationInfo>() {
                @Override
                public int compare(ApplicationInfo o1, ApplicationInfo o2) {
                    try {
                        long lhsInstallTime = getPackageManager().getPackageInfo(o1.packageName, 0).firstInstallTime;
                        long rhsInstallTime = getPackageManager().getPackageInfo(o2.packageName, 0).firstInstallTime;
                        return Long.compare(lhsInstallTime, rhsInstallTime);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
        }
        setDataViewPager();
        updateAdapter();
    }

    private void sortByName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mListApplicationInfo.sort(new Comparator<ApplicationInfo>() {
                @Override
                public int compare(ApplicationInfo o1, ApplicationInfo o2) {
                    String s1 = (String) getPackageManager().getApplicationLabel(o1);
                    String s2 = (String) getPackageManager().getApplicationLabel(o2);
                    return s1.compareToIgnoreCase(s2);
                }
            });
        }
        setDataViewPager();
        updateAdapter();
    }

    private void updateAdapter() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(mPagerAppAdapter);
                hideProgress();
            }
        }, 1000);
    }

}
