package project.meet;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;

public class MatchHolder extends RecyclerView.ViewHolder{
    CircleImageView profileImage;
    TextView name, tag, chatID;



    public MatchHolder(@NonNull View itemView) {
        super(itemView);

        profileImage=itemView.findViewById(R.id.profileImage);
        name=itemView.findViewById(R.id.name);
        tag=itemView.findViewById(R.id.tag);
        chatID=itemView.findViewById(R.id.chatID);


    }
/*
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(v.getContext(), ChatRoom.class);
        System.out.println("CHatID in chat display"+chatID.getText().toString());
        intent.putExtra("chatID",chatID.getText().toString());
        v.getContext().startActivity(intent);
    }
*/
}
