package project.meet;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesHolder> {
    private ArrayList<MessageObject> messagesList;
    private Context context;


    public MessagesRecyclerAdapter(ArrayList<MessageObject> messagesList, Context context){
        this.messagesList = messagesList;
        this.context = context;
    }

    @Override
    public MessagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message,null,false);
        return new MessagesHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesHolder holder, int position) {
        holder.msg.setText(messagesList.get(position).getMsg());
        if(messagesList.get(position).getSentByCurrentUser()){
            holder.msg.setBackgroundColor(Color.parseColor("#7400FF"));
            holder.msg.setGravity(Gravity.END);
        }
        else{
            holder.msg.setBackgroundColor(Color.parseColor("#000000"));
            holder.msg.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }
}
