package com.example.bronej.arsapprespondant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Region;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class makePollActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    TextView questionView;
    ImageButton confirm_choice;
    ArrayList<Option> options = new ArrayList<>();
    Question question;
    Integer selected_option;
    Intent intent;
    String session_key;
    private ProgressDialog progressDialog;


    OptionApiInterface optionApiInterface;
    public static String QUESTION_EXTRA="question";
    public static String OPTIONS_EXTRA="options";
    public static String SESSION_KEY="session_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_poll);

        intent = getIntent();
        confirm_choice = (ImageButton)findViewById(R.id.confirm_select_choice);
        questionView = (TextView)findViewById(R.id.question);
        radioGroup = (RadioGroup)findViewById(R.id.optionsgroup);
        setQuestion();
        setOptions();
        InitializeViews();

    }

    public void setQuestion(){

        session_key = intent.getStringExtra(SESSION_KEY);
        question = intent.getParcelableExtra(QUESTION_EXTRA);

    }

    public void setOptions(){

        options = intent.getParcelableArrayListExtra(OPTIONS_EXTRA);

    }

    public void showProgressBar(String message){

        progressDialog = new ProgressDialog(makePollActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }
    public void dismissProgressBar(){
        progressDialog.dismiss();
    }


    public void InitializeViews(){

        String qn = questionView.getText().toString() + question.getBody();
        questionView.setText(qn);

        float textSize = 18;

//        populate the radio button
        if (!(options==null)){

            for (Option option : options){
                RadioButton radioButton =  new RadioButton(makePollActivity.this);
                radioButton.setText(option.getOption_body());
                radioButton.setId(Integer.parseInt(option.getOption_ID()));
                radioButton.setTextSize(textSize);
                radioGroup.addView(radioButton);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
//                RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                    selected_option = checkedRadioButtonId;
                }
            });

            confirm_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateOptionChoices(selected_option);

                }
            });

        }else {
            Toast.makeText(this, "no options", Toast.LENGTH_SHORT).show();
        }


    }

    public void updateOptionChoices(int OptionId){

        showProgressBar("Updating Option...");
        optionApiInterface = ApiClient.getApiClient(makePollActivity.this).create(OptionApiInterface.class);
        Call<Option> call = optionApiInterface.updateoptionchoice(session_key,OptionId);
        call.enqueue(new Callback<Option>() {
            @Override
            public void onResponse(Call<Option> call, Response<Option> response) {
                dismissProgressBar();
                Toast.makeText(makePollActivity.this, "Option updated", Toast.LENGTH_SHORT).show();
                Log.e("response body",response.body().toString());
                Intent intent1 = new Intent(makePollActivity.this,MainActivity.class);
                startActivity(intent1);
            }

            @Override
            public void onFailure(Call<Option> call, Throwable t) {

                Toast.makeText(makePollActivity.this, "Cant update option", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
