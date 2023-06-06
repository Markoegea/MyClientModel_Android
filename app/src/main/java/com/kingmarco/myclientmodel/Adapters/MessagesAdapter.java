package com.kingmarco.myclientmodel.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.POJOs.Messages;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {
    private Fragment fragment;
    private ListView listView;
    private ArrayList<Messages> messagesArrayList;

    public MessagesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDatabase(ArrayList<Messages> messagesArrayList) {
        this.messagesArrayList = messagesArrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(listView != null){
            this.listView.smoothScrollToPosition(this.messagesArrayList.size());
        }
    }

    public void listViewToScroll(ListView listView) {
        this.listView = listView;
    }

    @Override
    public int getCount() {
        if(messagesArrayList == null){return 0;}
        return messagesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Messages messages = messagesArrayList.get(position);
        @SuppressLint("ViewHolder") View contentView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_list_item,parent,false);
        ViewHolderChatMessages chatMessages = new ViewHolderChatMessages(contentView,messages);
        chatMessages.setTextInfo();
        chatMessages.setMessageStyle();
        return contentView ;
    }

    private class ViewHolderChatMessages {

        private View contentView;
        private Messages messages;
        private RelativeLayout rlParent,rlMessage;
        private TextView txtMessageText, txtMessageTime, txtStatus;

        public ViewHolderChatMessages(View contentView,Messages messages) {
            this.contentView = contentView;
            this.messages = messages;
            rlParent = this.contentView.findViewById(R.id.rlParent);
            rlMessage = this.contentView.findViewById(R.id.rlMessage);
            txtMessageText = this.contentView.findViewById(R.id.txtMessageText);
            txtMessageTime = this.contentView.findViewById(R.id.txtMessageTime);
            txtStatus = this.contentView.findViewById(R.id.txtStatus);
        }

        public void setTextInfo(){
            txtMessageText.setText(messages.getText());
            txtMessageTime.setText(messages.getTimestamp().toString());
            switch (messages.getStatus()){
                case SENT:
                    txtStatus.setText("Enviado");
                    break;
                case RECEIVED:
                    txtStatus.setText("Recibido");
                    break;
                case SEEN:
                    txtStatus.setText("Visto");
                    break;
            }
        }

        public void setMessageStyle(){
            if(!SyncAuthDB.getInstance().isLogin()){return;}
            if(ClientHolder.getYouClient() == null){return;}
            if(messages.getSenderId() != ClientHolder.getYouClient().getMessagingId()){
                return;
            }
            txtStatus.setVisibility(View.VISIBLE);
            rlMessage.setBackground(ContextCompat.getDrawable(fragment.getContext(),R.drawable.ic_my_message));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlParent.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);
            rlParent.setLayoutParams(params);
        }
    }
}
