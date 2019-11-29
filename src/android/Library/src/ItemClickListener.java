package com.synconset;

import java.util.ArrayList;

/**
 * Author CodeBoy722
 */
public interface ItemClickListener {

    /**
     * Called when a picture is clicked
     * @param holder The ViewHolder for the clicked picture
     * @param position The position in the grid of the picture that was clicked
     */
    void onPicClicked(PicHolder holder, int position, ArrayList<PictureFacer> pics);
    void onPicClicked(String pictureFolderPath, String folderName);
}