package com.mibarim.main.models;

import java.io.Serializable;

/**
 * Created by Hamed on 4/19/2016.
 */
public class MessageModel implements Serializable {
    public int GroupId;
    public long CommentId;
    public String NameFamily;
    public String TimingString;
    public String Comment;
    public boolean IsDeletable;
    public String Base64UserPic;
    public String UserImageId;
}
