package project.meet;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    TextView name, tag;



    public MatchHolder(@NonNull View itemView) {
        super(itemView);

        profileImage=itemView.findViewById(R.id.profileImage);
        name=itemView.findViewById(R.id.name);
        tag=itemView.findViewById(R.id.tag);
    }
}
