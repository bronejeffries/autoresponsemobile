package com.example.bronej.arsapprespondant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentTopicActivity extends AppCompatActivity {
    TextView topicview;
    EditText comment_input;
    ImageButton confirm;
    Intent intent;
    Topic topic;
    String comment,question_ID,session_key;
    CommentApiInterface commentApiInterface;
    private ProgressDialog progressDialog;


    public static String TOPIC_EXTRA="topic";
    public static String SESSION_KEY = "session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_topic);

        topicview = (TextView)findViewById(R.id.topic);
        comment_input = (EditText)findViewById(R.id.comment_topic);
        confirm = (ImageButton)findViewById(R.id.confirm_comment);
        intent = getIntent();

        InitializeViews();

    }

    public void showProgressBar(String message){

        progressDialog = new ProgressDialog(CommentTopicActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }
    public void dismissProgressBar(){
        progressDialog.dismiss();
    }

    public void InitializeViews(){

        setTopicview();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                comment = comment_input.getText().toString();
//                todo: create comment with question id

                showProgressBar("Uploading...");
                commentApiInterface = ApiClient.getApiClient(CommentTopicActivity.this).create(CommentApiInterface.class);
                Comment new_comment = new Comment(comment,question_ID);
                Call<Comment> commentCall = commentApiInterface.createComment(session_key,new_comment);
                commentCall.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        dismissProgressBar();
                        Toast.makeText(CommentTopicActivity.this, "Comment uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {

                        Toast.makeText(CommentTopicActivity.this, "Cant create comment", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });

    }

    public void setTopicview(){

        session_key = intent.getStringExtra(SESSION_KEY);
        topic = intent.getParcelableExtra(TOPIC_EXTRA);
        String tpc = topicview.getText().toString() + topic.getBody();
        question_ID = String.valueOf(topic.getId());
        topicview.setText(tpc);

    }
}
