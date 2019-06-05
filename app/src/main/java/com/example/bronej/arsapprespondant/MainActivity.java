package com.example.bronej.arsapprespondant;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private CardView submissions, discussions, polls;
    private AlertDialog alertDialog;
    EditText link_input;
    ImageButton confirm;
    private QuestionApiInterface questionApiInterface;
    private ProgressDialog progressDialog;
    Question question;
    Topic topic;
    final String POLLS_SELECTED = "poll", DISCUSSION_SELECTED = "discussion", SUBMISSION_SELECTED = "submission";
    final int GALLERY_REQUEST = 20, RequestCameraPermissionId = 1001;
    String seleted;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    SurfaceView surfaceView;
    String link;


    private OptionApiInterface optionApiInterface;
    private ArrayList<Option> optionArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submissions = (CardView) findViewById(R.id.submissions);
        discussions = (CardView) findViewById(R.id.discussions);
        polls = (CardView) findViewById(R.id.polls);

        setListeners();

    }

    public void setListeners() {
        submissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSubmissions();
            }
        });

        discussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrcodeSelectionOption(DISCUSSION_SELECTED);
            }
        });

        polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePolls();
            }
        });
    }

    public void handleSubmissions() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = link_input.getText().toString();
                //                todo: show file submission activity and upload file to link
                alertDialog.dismiss();
            }
        };

        createLinkDialog(listener);

    }

    public void handleDiscussions(final String link_recieved) {
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

                showProgressBar("Fetching discussion data..");

                String[] link_divisions = link_recieved.split("/");
                String topicId = link_divisions[6];
                final String session_key = link_divisions[4];
                questionApiInterface = ApiClient.getApiClient(MainActivity.this).create(QuestionApiInterface.class);
                Call<Topic> call = questionApiInterface.getTopic(session_key, topicId);
                call.enqueue(new Callback<Topic>() {
                    @Override
                    public void onResponse(Call<Topic> call, Response<Topic> response) {

                        if (!(response.body() == null)) {

                            topic = response.body();
//                            alertDialog.dismiss();
                            dismissProgressBar();
                            Intent intent = new Intent(MainActivity.this, CommentTopicActivity.class);
                            intent.putExtra(CommentTopicActivity.TOPIC_EXTRA, topic);
                            intent.putExtra(CommentTopicActivity.SESSION_KEY, session_key);
                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "check connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Topic> call, Throwable t) {

                    }
                });
//                todo: fetch question and allow making comments in the commentTopic activity

//            }
//        };

//        createLinkDialog(listener);
    }

    public void handlePolls() {
        qrcodeSelectionOption(POLLS_SELECTED);
    }

    public void StartPollsIntent(final String link_recieved) {

        //                todo: fetch question and options created and show them in the make_poll activity

        showProgressBar("fetching polls data..");

//        final String link = link_input.getText().toString();

        String[] link_divisions = link_recieved.split("/");
        String questionId = link_divisions[6];
        final String session_key = link_divisions[4];

        questionApiInterface = ApiClient.getApiClient(MainActivity.this).create(QuestionApiInterface.class);
        Call<Question> call = questionApiInterface.getQuestion(session_key, questionId);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {

                if (!(response.body() == null)) {

                    question = response.body();
                    optionApiInterface = ApiClient.getApiClient(link_recieved).create(OptionApiInterface.class);
                    Log.e("fetch question", link_recieved);
                    Call<List<Option>> optionsCall = optionApiInterface.getOptions();
                    optionsCall.enqueue(new Callback<List<Option>>() {
                        @Override
                        public void onResponse(Call<List<Option>> call, Response<List<Option>> response) {

                            if (!(response.body() == null)) {

                                optionArrayList = (ArrayList<Option>) response.body();
//                                alertDialog.dismiss();
                                dismissProgressBar();
                                Intent intent = new Intent(MainActivity.this, makePollActivity.class);
                                intent.putExtra(makePollActivity.QUESTION_EXTRA, question);
                                intent.putExtra(makePollActivity.OPTIONS_EXTRA, optionArrayList);
                                intent.putExtra(makePollActivity.SESSION_KEY, session_key);
                                startActivity(intent);

                            } else {
                                Toast.makeText(MainActivity.this, "check connectivity", Toast.LENGTH_SHORT).show();
                                dismissProgressBar();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Option>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                            dismissProgressBar();

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Check connectivity", Toast.LENGTH_SHORT).show();
                    dismissProgressBar();
                }

            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {

                Toast.makeText(MainActivity.this, "cant fetch question", Toast.LENGTH_SHORT).show();
                dismissProgressBar();
                t.printStackTrace();

            }
        });


    }


    public void showProgressBar(String message) {

//        dismiss camera view surface
        alertDialog.dismiss();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }

    public void dismissProgressBar() {
        progressDialog.dismiss();
    }

    public void qrcodeSelectionOption(final String choice_selected) {
        AlertDialog.Builder selectionDialog = new AlertDialog.Builder(MainActivity.this);
        final CharSequence[] items = {"Scan", "Cancel"};
        selectionDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (position) {
//                    case 0:
////                        todo: choose from gallery
//                        chooseFromGallery(choice_selected);
//                        break;
                    case 0:
//                        todo: scan qrcode
                        scanQrCodeView(choice_selected);
                        break;
                    case 1:
                        dialogInterface.dismiss();
                }
            }
        });
        selectionDialog.show();
    }

    public void chooseFromGallery(String choice_selected) {
        seleted = choice_selected;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionId:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void scanQrCodeView(final String choice_selected) {

        final TextView resultview;
        final Button continue_btn;
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.scanqrcode, null, false);
        surfaceView = (SurfaceView) relativeLayout.findViewById(R.id.cameraPreview);
        resultview = (TextView) relativeLayout.findViewById(R.id.resultView);
        continue_btn = (Button) relativeLayout.findViewById(R.id.continue_btn);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (choice_selected){
                    case POLLS_SELECTED:
                        StartPollsIntent(link);
                        break;
                    case DISCUSSION_SELECTED:
                        handleDiscussions(link);
                        break;
                    default:
                        StartPollsIntent(link);
                        break;
                }

            }
        });

        barcodeDetector = new BarcodeDetector
                .Builder(MainActivity.this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(MainActivity.this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

//        add event
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},RequestCameraPermissionId);
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                Log.e("recieved detections",String.valueOf(detections.getDetectedItems()));
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0 ){

                    resultview.post(new Runnable() {
                        @Override
                        public void run() {
//                            create vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator != null) {
                                vibrator.vibrate(1000);
                            }
                            link = qrcodes.valueAt(0).displayValue;
                            resultview.setText(link);
                            continue_btn.setVisibility(View.VISIBLE);

                        }
                    });
                }

            }
        });

        dialog.setView((View)relativeLayout);
        alertDialog = dialog.create();
        alertDialog.show();

    }

    public void startQrcodeScanner(Bitmap bitmap){

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(MainActivity.this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        Frame frame = new Frame.Builder()
                .setBitmap(bitmap).build();
        SparseArray<Barcode> barsCode = barcodeDetector.detect(frame);
        Barcode result = barsCode.valueAt(0);
        String linkdata = result.rawValue;

        switch (seleted){
            case POLLS_SELECTED:
                StartPollsIntent(linkdata);
                break;


        }


    }


    public void createLinkDialog(View.OnClickListener listener){

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.enter_link,null,false);
        link_input=(EditText)relativeLayout.findViewById(R.id.input_link);
        confirm = (ImageButton)relativeLayout.findViewById(R.id.confirm_link);

        confirm.setOnClickListener(listener);
        dialog.setView((View)relativeLayout);
        alertDialog = dialog.create();
        alertDialog.show();

    }
}
