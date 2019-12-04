package com.synconset;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import io.ionic.starter.R;

/**
 * Author CodeBoy722
 * <p>
 * An adapter for populating RecyclerView with items representing folders that contain images
 */
public class PictureFolderAdapter extends RecyclerView.Adapter<PictureFolderAdapter.FolderHolder> {
    private ArrayList<PictureFacer> folders;
    private Context folderContx;
    private ItemClickListener listenToClick;

    /**
     * @param folders     An ArrayList of String that represents paths to folders on the external storage that contain pictures
     * @param folderContx The Activity or fragment Context
     * @param listen      interFace for communication between adapter and fragment or activity
     */
    public PictureFolderAdapter(ArrayList<PictureFacer> folders, Context folderContx, ItemClickListener listen) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.list_picture_single_item, parent, false);
        return new FolderHolder(cell);

    }

    boolean selected = false;

    @Override
    public void onBindViewHolder(@NonNull final FolderHolder holder, int position) {
        final PictureFacer folder = folders.get(position);
        Glide.with(folderContx)
                .load(folder.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folderPic);

        //  String text = "("+folder.getNumberOfPics()+") "+folder.getFolderName();
        //   holder.folderName.setText(text);

        if (folder.getSelected()) {
            holder.folderimageTick.setVisibility(View.VISIBLE);
        } else {
            holder.folderimageTick.setVisibility(View.GONE);
        }

        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folder.getSelected()) {
                    holder.folderimageTick.setVisibility(View.GONE);
                    selected = false;
                    folder.setSelected(false);
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        holder.folderPic.setImageAlpha(255);
                    } else {
                        holder.folderPic.setAlpha(255);
                    }
                    holder.frameLayout.setBackgroundColor(Color.TRANSPARENT);
                    ChooseImageActivity.imagesPathSet.remove(folder.getPicturePath());
                    ChooseImageActivity.countImages--;
                    if (folderContx instanceof IMethodCaller) {
                        ((IMethodCaller) folderContx).yourDesiredMethod(ChooseImageActivity.countImages);
                    }
                } else {
                    if (ChooseImageActivity.maxImageCount > ChooseImageActivity.countImages) {
                        long lengthOfFileinKb = 0;
                        if (folder.getPicturName() != null && folder.getPicturName().length() > 0) {
                            lengthOfFileinKb = Utils.checkFileSize(folder.getPicturePath() + "/" + folder.getPicturName());
                        } else {
                            //All Photos case
                            lengthOfFileinKb = Utils.checkFileSize(folder.getPicturePath());
                        }
                        if (lengthOfFileinKb > ChooseImageActivity.quality) {
                            selected = true;
                            holder.folderimageTick.setVisibility(View.VISIBLE);
                            folder.setSelected(true);
                            // Add that value in list
                            if (android.os.Build.VERSION.SDK_INT >= 16) {
                                holder.folderPic.setImageAlpha(128);
                            } else {
                                holder.folderPic.setAlpha(128);
                            }
                            holder.frameLayout.setBackgroundColor(selectedColor);
                            listenToClick.onPicClicked(folder.getPicturePath(), folder.getPicturName());
                            ChooseImageActivity.imagesPathSet.add(folder.getPicturePath());
                            ChooseImageActivity.countImages++;
                            if (folderContx instanceof IMethodCaller) {
                                ((IMethodCaller) folderContx).yourDesiredMethod(ChooseImageActivity.countImages);
                            }
                        } else {
                            Utils.askForInput(folderContx, "Low Quality Image!", "Please select a good quality image!\n(Note: Image should be greater than 20 kb).", "Don't Select", "Select Anyway", false, new AlertDialogCallback<String>() {
                                @Override
                                public void alertDialogCallback(String callback) {
                                    if (callback == "1") {
                                        //Case : Don't Select
                                        // Simple Dialog close
                                    } else {
                                        // Add that image to array
                                        selected = true;
                                        holder.folderimageTick.setVisibility(View.VISIBLE);
                                        folder.setSelected(true);
                                        // Add that value in list
                                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                                            holder.folderPic.setImageAlpha(128);
                                        } else {
                                            holder.folderPic.setAlpha(128);
                                        }
                                        holder.frameLayout.setBackgroundColor(selectedColor);
                                        listenToClick.onPicClicked(folder.getPicturePath(), folder.getPicturName());
                                        ChooseImageActivity.imagesPathSet.add(folder.getPicturePath());
                                        ChooseImageActivity.countImages++;
                                        if (folderContx instanceof IMethodCaller) {
                                            ((IMethodCaller) folderContx).yourDesiredMethod(ChooseImageActivity.countImages);
                                        }
                                    }
                                }
                            });
                        }
                    }  else {
                    Utils.askForInput(folderContx, "Alert!", "Max image select count reached.Default Image count is " + ChooseImageActivity.maxImageCount, "Ok", "Cancel", true, new AlertDialogCallback<String>() {
                        @Override
                        public void alertDialogCallback(String callback) {

                        }
                    });

                    Log.d("Onclick", "onClick: " + ChooseImageActivity.maxImageCount + " , " + ChooseImageActivity.countImages);
                }

                }
            }

        });
    }

    private int selectedColor = 0xff32b2e1;

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder {
        ImageView folderPic, folderimageTick;
        FrameLayout frameLayout;
        // TextView folderName;
        //CardView folderCard;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.frame_layout);
            folderPic = itemView.findViewById(R.id.image);
            folderimageTick = itemView.findViewById(R.id.image_tick);
            //  folderName = itemView.findViewById(R.id.folderName);
            //  folderCard = itemView.findViewById(R.id.folderCard);
        }
    }

}