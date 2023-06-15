package com.kingmarco.myclientmodel.Fragments.Chat;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kingmarco.myclientmodel.Adapters.MessagesAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ChatHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.NotificationManager;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncAuthDB;
import com.kingmarco.myclientmodel.POJOs.TimestampDeserializer;
import com.kingmarco.myclientmodel.Auxiliary.Enums.MessagesStatus;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ChatObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.POJOs.Messages;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

/**The fragment responsible for show the messages from the firebase*/
public class ChatFragment extends Fragment implements ChatObserver {
    private SetLabelName setLabelName;
    private View contentView;
    private ListView lvMessages;
    private EditText edtMessage;
    private Button btnSent;
    private DatabaseReference databaseReference;
    private MessagesAdapter messagesAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();

        messagesAdapter = new MessagesAdapter(this);
        Clients clients = ClientHolder.getYouClient();
        if(clients == null){return;}
        databaseReference = FirebaseDatabase.getInstance().getReference("chats/"+clients.getMessagingId()+"/messages");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_chat, container, false);

        lvMessages = contentView.findViewById(R.id.lvMessages);
        edtMessage = contentView.findViewById(R.id.edtMessage);
        btnSent = contentView.findViewById(R.id.btnSent);

        lvMessages.setAdapter(messagesAdapter);
        /**Add and push a new message to the real time database**/
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = edtMessage.getText().toString();
                Clients clients = ClientHolder.getYouClient();
                if (messageText.isEmpty()){return;}
                if(!SyncAuthDB.getInstance().isLogin()){return;}
                if(ClientHolder.getYouClient() == null){return;}
                DatabaseReference messagesReference = databaseReference.push();
                Messages messages = new Messages(clients.getMessagingId(),
                        clients.getName(),
                        clients.getImageUrl(),
                        messageText,
                        MessagesStatus.SENT,
                        new TimestampDeserializer(Timestamp.now().getSeconds(),Timestamp.now().getNanoseconds()));
                messagesReference.setValue(messages);
                edtMessage.setText("");
            }
        });

        /** Set an OnGlobalLayoutListener to listen to the keyboard visibility changes*/
        View rootView = getActivity().getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keyboardHeight = screenHeight - r.bottom;
                if (keyboardHeight > screenHeight * 0.15) { // Keyboard is shown
                    lvMessages.postDelayed(() -> lvMessages.smoothScrollToPosition(messagesAdapter.getCount()), 0);
                }
            }
        });
        messagesAdapter.listViewToScroll(lvMessages);

        setLabelName.setLabelName("Chat");
        // Inflate the layout for this fragment
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ChatHolder.addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ChatHolder.removeObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ChatHolder.removeObserver(this);
    }

    @Override
    public void onMessagesChange(ArrayList<Messages> arrayList) {
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}
        for (Messages message : arrayList){
            if(message.getSenderId() != ClientHolder.getYouClient().getMessagingId()
                    && message.getStatus() != MessagesStatus.SEEN){
                message.setStatus(MessagesStatus.SEEN);
                NotificationManager.getInstance().deleteMessagesNotification(message.getSenderId());
                databaseReference.child(message.getId()).setValue(message);
            }
        }
        messagesAdapter.setDatabase(arrayList);
    }

    @Override
    public void onChatChange(Chats chats) {

    }
}