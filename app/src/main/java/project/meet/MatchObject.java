package project.meet;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

public class MatchObject {
    private String userID;
    private String name;
    private String tag;
    private StorageReference imageRef;

    public MatchObject (String userID, String name, String tag, StorageReference imageRef){
        this.userID = userID;
        this.name = name;
        this.tag = tag;
        this.imageRef = imageRef;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getTag(){
        return tag;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public StorageReference getImageRef(){
        return imageRef;
    }

    public void setImageRef(StorageReference profileImageUrl){
        this.imageRef = profileImageUrl;
    }
}
