package project.meet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<MatchHolder> {
    private List<MatchObject> matchesList;
    private Context context;


    public recyclerAdapter(List<MatchObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }



    @Override
    public MatchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_chatlist,null,false);
        return new MatchHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchHolder holder, int position) {
        matchesList.get(position).getImageRef().getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.profileImage));
        holder.name.setText(matchesList.get(position).getName());
        holder.tag.setText(matchesList.get(position).getTag());
        holder.chatID.setText(matchesList.get(position).getchatID());
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("position" + position);
                System.out.println("position[1]" + position);
                Intent intent = new Intent(v.getContext(), ChatRoom.class);
                System.out.println("CHatID in chat display" + matchesList.get(position).getchatID());
                System.out.println("CHatID in chat display" + matchesList.get(position).getName());
                intent.putExtra("chatID", matchesList.get(position).getchatID());
                v.getContext().startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }




}
