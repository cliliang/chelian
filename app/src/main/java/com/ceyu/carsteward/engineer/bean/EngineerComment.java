package com.ceyu.carsteward.engineer.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/5.
 */
public class EngineerComment {
    private ArrayList<CommentContent> commentContents;
//    private CommentGood commentGood;
    private String sumValue;
    public EngineerComment() {
    }

    public ArrayList<CommentContent> getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(ArrayList<CommentContent> commentContents) {
        this.commentContents = commentContents;
    }

    public String getSumValue() {
        return sumValue;
    }

    public void setSumValue(String sumValue) {
        this.sumValue = sumValue;
    }

    static EngineerComment fromString(String resource){
        EngineerComment comment = new EngineerComment();
        try {
            JSONObject object = new JSONObject(resource);
            String item = "list";
            if (object.has(item)){
                String list = object.optString(item);
                ArrayList<CommentContent> contents = CommentContent.fromString(list);
                comment.setCommentContents(contents);
            }
            item = "sum";
            if (object.has(item)){
                comment.setSumValue(object.optString(item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comment;
    }
}


