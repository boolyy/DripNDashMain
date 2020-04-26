package edu.rutgers.dripndashproject;

public class DasherInProgressItem {
    private int mImageResource; //image will depend on
    private String mText1; //maybe room number
    private String mText2; //maybe name

    public DasherInProgressItem(int imageResource, String text1, String text2){
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public void changeText1(String text){
        mText1 = text;
    }
    public int getmImageResource(){
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }
    public String getText2(){
        return mText2;
    }
}
