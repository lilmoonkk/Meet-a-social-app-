package project.meet;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesHolder extends RecyclerView.ViewHolder{

    public TextView msg;

    public MessagesHolder(@NonNull View itemView) {
        super(itemView);
        msg=itemView.findViewById(R.id.msgBox);
    }
}
