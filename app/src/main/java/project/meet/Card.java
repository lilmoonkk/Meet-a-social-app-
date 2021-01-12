package project.meet;

import com.google.firebase.storage.StorageReference;

public class Card {
    private String userID;
    private String name;
    private String age;
    private String tag;
    private StorageReference imageRef;

    public Card (String userID, String name, String age, String tag, StorageReference imageRef){
        this.userID = userID;
        this.name = name;
        this.age = age;
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

    public String getAge(){
        return age;
    }

    public void setAge(String age){
        this.age = age;
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
