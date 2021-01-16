package project.meet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<MatchHolder>{
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
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
