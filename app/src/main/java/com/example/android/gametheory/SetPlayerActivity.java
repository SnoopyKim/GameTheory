package com.example.android.gametheory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetPlayerActivity extends AppCompatActivity {
    private static final String TAG = ".SetPlayerActivity";

    DatabaseReference playerRef;

    ImageView ivPlayerProfile;
    EditText etPlayerName, etPlayerNote;
    Button btnStatus1, btnStatus2;

    String uid;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_player);

        Player player = (Player)getIntent().getSerializableExtra("player");

        uid = player.getUid();
        playerRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        ivPlayerProfile = findViewById(R.id.iv_profile);
        image = player.getProfile();
        if (image != null) {
            Glide.with(this).load(image).into(ivPlayerProfile);
        }

        etPlayerName = findViewById(R.id.et_player_name);
        etPlayerName.setText(player.getName());

        etPlayerNote = findViewById(R.id.et_special_note_value);
        etPlayerNote.setText(player.getNote());

        btnStatus1 = findViewById(R.id.btn_status_value_1);
        btnStatus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    btnStatus1.setSelected(true);
                    btnStatus1.setTextColor(getColor(R.color.white));
                    btnStatus2.setSelected(false);
                    btnStatus2.setTextColor(getColor(R.color.black));
                }
            }
        });
        btnStatus2 = findViewById(R.id.btn_status_value_2);
        btnStatus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    btnStatus2.setSelected(true);
                    btnStatus2.setTextColor(getColor(R.color.white));
                    btnStatus1.setSelected(false);
                    btnStatus1.setTextColor(getColor(R.color.black));
                }
            }
        });
        if (player.isStatus()) {
            btnStatus1.setSelected(true);
            btnStatus1.setTextColor(getColor(R.color.white));
            btnStatus2.setSelected(false);
            btnStatus2.setTextColor(getColor(R.color.black));
        } else {
            btnStatus2.setSelected(true);
            btnStatus2.setTextColor(getColor(R.color.white));
            btnStatus1.setSelected(false);
            btnStatus1.setTextColor(getColor(R.color.black));
        }
    }

    public void changeProfileImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "사진을 고르세요"), 1);
    }

    public void changeProfileInfo(View view) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("player").child(uid);
        storageRef.putFile(Uri.parse(image)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (!task.isSuccessful()) { CustomUtils.displayToast(getApplicationContext(), "이미지 등록에 실패했습니다."); }
                else {
                    String stUri = task.getResult().getStorage().getDownloadUrl().toString();
                    playerRef.child("profile").setValue(stUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                CustomUtils.displayToast(getApplicationContext(), "플레이어 정보가 변경되었습니다.");
                                finish();
                            }
                        }
                    });
                }
            }
        });

        String stName = etPlayerName.getText().toString();
        playerRef.child("name").setValue(stName);

        String stNote = etPlayerNote.getText().toString();
        playerRef.child("note").setValue(stNote);

        boolean status = btnStatus1.isSelected();
        playerRef.child("status").setValue(status);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && data!=null) {
            Uri fileUri = data.getData();
            image = String.valueOf(fileUri);
            Glide.with(this).load(image).into(ivPlayerProfile);
        }
    }

}
