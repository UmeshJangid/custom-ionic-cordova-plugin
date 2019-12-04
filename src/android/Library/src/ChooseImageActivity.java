package com.synconset;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.ionic.starter.R;

public class ChooseImageActivity extends AppCompatActivity implements IMethodCaller {
    String path;
    Toolbar toolbar;
    ViewPager viewPager;
    // PagerSlidingTabStrip tabs;
    TabLayout tabLayout;
    TextView textViewImageCount, textViewPrice;
    TabsPagerAdapter tabsPagerAdapter;
    Button buttonSubmit;
    private MyPagerAdapter adapter;
    private Drawable oldBackground = null;
    private int currentColor;
    ArrayList<ImageFolderDetail> imageFolderDetails;
    //  private SystemBarTintManager mTintManager;
    private static final String TAG = "ChooseImageActivity";
    public static Set<String> imagesPathSet;
    public static int countImages = 0;
    private FakeR fakeR;

    public static final int NOLIMIT = -1;
    public static final String MAX_IMAGES_KEY = "MAX_IMAGES";
    public static final String QUALITY_KEY = "QUALITY";
    public static final String PRICE_KEY = "PRICE";
    public static final String COLOR_KEY = "COLOR";
    public static final String TITLE_KEY = "TITLE";
    public static final String BUTTON_TEXT_KEY = "BUTTON_TEXT_KEY";
    public static final String OUTPUT_TYPE_KEY = "OUTPUT_TYPE";


    private int maxImages;
    public static int maxImageCount = 0;
    private int desiredWidth;
    private int desiredHeight;
    private int price = 0;
    private String bgcolor;
    private String btnText;
    private String toolbarTitle;
    public static int quality;


    private MultiImageChooserActivity.OutputType outputType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fakeR = new FakeR(this);
        setContentView(fakeR.getId("layout", "activity_choose_image"));
        // setContentView(R.layout.activity_choose_image);
        imageFolderDetails = getPicturePaths();
        imagesPathSet = new HashSet<>();
        try {
            maxImages = getIntent().getIntExtra(MAX_IMAGES_KEY, NOLIMIT);
            quality = getIntent().getIntExtra(QUALITY_KEY, 20);
            price = getIntent().getIntExtra(PRICE_KEY, 0);
            bgcolor = getIntent().getStringExtra(COLOR_KEY);
            toolbarTitle = getIntent().getStringExtra(TITLE_KEY);
            btnText = getIntent().getStringExtra(BUTTON_TEXT_KEY);
            maxImageCount = maxImages;
            // maxImageCount =4;
            outputType = MultiImageChooserActivity.OutputType.fromValue(getIntent().getIntExtra(OUTPUT_TYPE_KEY, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* for (ImageFolderDetail imageFolderDetail : imageFolderDetails
        ) {
            Log.e(TAG, imageFolderDetail.getFolderName() + ", Path :" + imageFolderDetail.getPath());
        }*/

        viewPager = (ViewPager) findViewById(R.id.pager);
        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        textViewImageCount = (TextView) findViewById(R.id.txt_imageCount);
        textViewPrice = (TextView) findViewById(R.id.priceTileslabel);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        //    tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Title Set
        if (toolbarTitle != null && toolbarTitle.length() > 0) {
            toolbar.setTitle(toolbarTitle);
        }
        //Button Text
        if (btnText != null && btnText.length() > 0) {
            buttonSubmit.setText(btnText);
        }
        // Price text
        if (price > 0) {
            String pound = "\u00a3";
            textViewPrice.setText("GBP " + pound + "" + price);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs_new);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        adapter = new MyPagerAdapter(getSupportFragmentManager());
     /*   tabs.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                Toast.makeText(ChooseImageActivity.this, "Tab reselected: " + position, Toast.LENGTH_SHORT).show();
            }
        });*/
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //  Toast.makeText(ChooseImageActivity.this, "Tab selected: " + tab.getPosition(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setOffscreenPageLimit(1);
        addFrag();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesPathSet.size() > 0) {
                    Utils.askForInput(ChooseImageActivity.this, "Alert!", "Are you sure you want move forward?", "Yes", "No", false, new AlertDialogCallback<String>() {
                        @Override
                        public void alertDialogCallback(String callback) {
                            // Go to
                            if (callback.equals("1")) {
                                for (String e : imagesPathSet
                                ) {
                                    Log.e("Selected Images", e);
                                }
                                Intent dataIntent = new Intent();
                                if (imagesPathSet.size() > 0) {
                                    Bundle res = new Bundle();
                                    ArrayList<String> list = new ArrayList<String>(imagesPathSet);
                                    res.putStringArrayList("MULTIPLEFILENAMES", list);
                                    res.putInt("TOTALFILES", list.size());
                                    int sync = ResultIPC.get().setLargeData(res);
                                    dataIntent.putExtra("bigdata:synccode", sync);
                                    setResult(RESULT_OK, dataIntent);
                                } else {
                                    Bundle res = new Bundle();
                                    res.putString("ERRORMESSAGE", "No Image Selected");
                                    dataIntent.putExtras(res);
                                    setResult(RESULT_CANCELED, dataIntent);
                                }
                                onBackPressed();
                            }
                        }
                    });


                } else {
                    Utils.askForInput(ChooseImageActivity.this, "Alert!", "Please select a image to move forward", "ok", "No", true, new AlertDialogCallback<String>() {
                        @Override
                        public void alertDialogCallback(String callback) {
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void addFrag() {
        if (imageFolderDetails != null && imageFolderDetails.size() > 0) {
            for (int i = 0; i < imageFolderDetails.size(); i++) {
                ImageFolderDetail imageFolderDetail = imageFolderDetails.get(i);
                Fragment fragment = ImageFragment.newInstance(imageFolderDetail.getFolderName(), i, imageFolderDetail.getPath());
                tabsPagerAdapter.addFragment(fragment, imageFolderDetail.getFolderName());
                Log.e(TAG, imageFolderDetail.getFolderName() + ", Path :" + imageFolderDetail.getPath());
            }

        } else {
            Utils.showToast(ChooseImageActivity.this, "Something went wrong.");
        }
        viewPager.setAdapter(tabsPagerAdapter);
        // tabs.setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        // viewPager.setOffscreenPageLimit(imageFolderDetails.size());

    }

    public static ArrayList<String> allImg;

    /**
     * @return gets all folders with pictures on the device and loads each of them in a custom object imageFolder
     * the returns an ArrayList of these custom objects
     */
    private ArrayList<ImageFolderDetail> getPicturePaths() {
        ArrayList<ImageFolderDetail> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        allImg = new ArrayList<>();
        String firstFolder = "All Photos";
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            // Adding for all images
            picFolders.add(new ImageFolderDetail("all", firstFolder));
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                ImageFolderDetail folds = new ImageFolderDetail();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                allImg.add(datapath);
                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);
                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }
        return picFolders;
    }

    @Override
    public void yourDesiredMethod(int count) {
        Log.e(TAG, "yourDesiredMethod: " + count);
        textViewImageCount.setText(ChooseImageActivity.imagesPathSet.size() + " Image selected");
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
                "Top New Free", "Trending"};

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            // return SuperAwesomeCardFragment.newInstance(position);
            return null;
        }
    }


}
