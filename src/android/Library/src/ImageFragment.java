package com.synconset;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.ionic.starter.R;

public class ImageFragment extends Fragment implements ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam3;
    private Integer mParam2;
    private static final String TAG = "ImageFragment";
    private Context mContext;
    private ArrayList<PictureFacer> pictureFacers;
    RecyclerView recyclerView;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String param1, Integer param2, String param3) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            //  Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]"+mParam2+","+mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = container.getContext();
        //   Log.d(TAG, "onCreateView() called with: savedInstanceState = [" + savedInstanceState + "]"+mParam2+","+mParam1);
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.recylerview);
        return view;
    }

    private void initRec() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(new PictureFolderAdapter(pictureFacers, mContext, ImageFragment.this));
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Intialize
        pictureFacers = new ArrayList<>();
        Log.d(TAG, "onViewCreated() called with: savedInstanceState = [" + savedInstanceState + "]" + mParam2 + "," + mParam1);
        if (mParam3.equalsIgnoreCase("all")) {
            for (String path : ChooseImageActivity.allImg
            ) {
                pictureFacers.add(new PictureFacer(path));
            }
        } else {
            pictureFacers = getAllImagesByFolder(mParam3);

        }
        initRec();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This Method gets all the images in the folder paths passed as a String to the method and returns
     * and ArrayList of pictureFacer a custom object that holds data of a given image
     *
     * @param path a String corresponding to a folder path on the device external storage
     */
    public ArrayList<PictureFacer> getAllImagesByFolder(String path) {
        if (path != null && path.length() > 0) {
            ArrayList<PictureFacer> images = new ArrayList<>();
            Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};
            Cursor cursor = mContext.getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
            try {
                cursor.moveToFirst();
                do {
                    PictureFacer pic = new PictureFacer();
                    pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                    pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                    pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                    images.add(pic);
                } while (cursor.moveToNext());
                cursor.close();
                ArrayList<PictureFacer> reSelection = new ArrayList<>();
                for (int i = images.size() - 1; i > -1; i--) {
                    reSelection.add(images.get(i));
                }
                images = reSelection;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return images;
        } else {
            Utils.showToast(mContext, "Oops something went wrong.");
            return null;
        }
    }

    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<PictureFacer> pics) {
        Log.d(TAG, "onPicClicked() called with: holder = [" + holder + "], position = [" + position + "], pics = [" + pics + "]");
        // Add image to set than  move forward.
    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        Log.d(TAG, "onPicClicked() called with: pictureFolderPath = [" + pictureFolderPath + "], folderName = [" + folderName + "]");
    }
}
