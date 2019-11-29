package com.synconset;

/**
 * Created by Umesh Jangid on 21-11-2019.
 * Company : Dotsquares Ltd.
 * Email: umesh.jangid@dotsquares.com
 * Package : com.example.sample.Models
 */
public class ImageFolderDetail {
    private String path;
    private String FolderName;
    private int numberOfPics = 0;
    private String firstPic;

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public void addpics(){
        this.numberOfPics++;
    }
    public ImageFolderDetail() {

    }
    public ImageFolderDetail(String path, String folderName) {
        this.path = path;
        FolderName = folderName;
    }
    public ImageFolderDetail(String path, String folderName, int numberOfPics) {
        this.path = path;
        FolderName = folderName;
        this.numberOfPics = numberOfPics;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public int getNumberOfPics() {
        return numberOfPics;
    }

    public void setNumberOfPics(int numberOfPics) {
        this.numberOfPics = numberOfPics;
    }
}
