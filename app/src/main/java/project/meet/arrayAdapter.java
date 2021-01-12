package project.meet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Card> {
   Context context;

   public arrayAdapter(Context context, int resourceID, List<Card> items){
       super(context, resourceID, items);
   }

   public View getView(int position, View convertView, ViewGroup parent){
       Card card=getItem(position);

       if (convertView == null){
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
       }

       TextView uName = (TextView) convertView.findViewById(R.id.name);
       TextView uAge = (TextView) convertView.findViewById(R.id.age);
       TextView uTag = (TextView) convertView.findViewById(R.id.tag);
       ImageView profileImage = (ImageView) convertView.findViewById(R.id.profileImage);

       uName.setText(card.getName());
       uAge.setText(card.getAge());
       uTag.setText(card.getTag());
       card.getImageRef().getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

       return convertView;
   }

}
