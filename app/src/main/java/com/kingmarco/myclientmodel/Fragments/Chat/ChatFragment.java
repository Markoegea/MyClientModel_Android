package com.kingmarco.myclientmodel.Fragments.Chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kingmarco.myclientmodel.Auxiliary.MessagesStatus;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Messages;
import com.kingmarco.myclientmodel.R;

import java.util.HashMap;
import java.util.Map;

/**The fragment responsible for show the messages from the firebase*/
//TODO: THE REFERENCE OF THE MESSAGES WILL BE THE NAME OF THE CLIENT, AND ORGANIZE THE MESSAGE CORRECTLY IN THE REALTIME DATABASE
public class ChatFragment extends Fragment {

    //TODO: TESTING PURPOSES
    private Integer id = 101;

    private SetLabelName setLabelName;
    private ListView lvMessages;
    private EditText edtMessage;
    private Button btnSent;
    private DatabaseReference databaseReference;
    private FirebaseListAdapter<Chats> firebaseListAdapter;
    private Chats chats;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        /**Get the reference of the realtime database of firebase*/
        databaseReference = FirebaseDatabase.getInstance().getReference("chats/"+id+"/");
        /**Get the reference to a layout and a class to organize the information*/
        /*FirebaseListOptions<Messages> options = new FirebaseListOptions.Builder<Messages>()
                .setLayout(R.layout.message_list_item)
                .setQuery(databaseReference, Messages.class)
                .build();*/

        /**Wit the information organized set and show it in the Text views*/
        /*firebaseListAdapter = new FirebaseListAdapter<>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Messages model, int position) {
                TextView txtMessageUser, txtMessageText, txtMessageTime;
                txtMessageUser = v.findViewById(R.id.txtMessageUser);
                txtMessageText = v.findViewById(R.id.txtMessageText);
                txtMessageTime = v.findViewById(R.id.txtMessageTime);

                txtMessageUser.setText(model.getMessageClient());
                txtMessageText.setText(model.getMessageText());
                txtMessageTime.setText(model.getMessageTime());
            }
        };*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        lvMessages = view.findViewById(R.id.lvMessages);
        edtMessage = view.findViewById(R.id.edtMessage);
        btnSent = view.findViewById(R.id.btnSent);

        lvMessages.setAdapter(firebaseListAdapter);
        /**Add and push a new message to the real time database**/
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edtMessage.getText().toString();
                if (!message.isEmpty()){
                    //TODO THE CHAT CREATION HAPPEN WHEN THE CLIENT REGISTER ITSELF
                    /*chats = new Chats(Long.parseLong(id.toString()),"gs://first-model-client.appspot.com/woman.png","Lucy");
                    databaseReference.setValue(chats);*/

                    DatabaseReference messagesReference = databaseReference.child("messages").push();
                    Messages messages = new Messages(message,"Scarlett",Timestamp.now().toDate(), MessagesStatus.SENT);
                    messagesReference.setValue(messages);
                    edtMessage.setText("");
                }
            }
        });

        setLabelName.setLabelName("Chat");
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //firebaseListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //firebaseListAdapter.stopListening();
    }
}