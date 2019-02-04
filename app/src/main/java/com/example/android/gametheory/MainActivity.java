package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = ".MainActivity";

    TextView tvTitle, tvSubTitle, tvTimer;

    FirebaseUser user;
    DatabaseReference gameRef;
    int currentGame = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("timer").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) { Log.d("subscribeToTopic", "success"); }
                else { Log.d("subscribeToTopic", "failed"); }
            }
        });

        tvTitle = findViewById(R.id.tv_title);
        tvSubTitle = findViewById(R.id.tv_subtitle);
        tvTimer = findViewById(R.id.tv_timer);

        user = FirebaseAuth.getInstance().getCurrentUser();
        gameRef = FirebaseDatabase.getInstance().getReference("game");
        gameRef.child("now").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentGame = ((Long)dataSnapshot.getValue()).intValue();
                gameRef.child(String.valueOf(currentGame)).child("time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long time = (long)dataSnapshot.getValue();
                        long minute = time/60;
                        long second = (time%60);
                        tvTimer.setText(String.format("%02d : %02d", minute,second));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        changeSubtitle(true);
        fragmentReplace(0);

        ImageButton btnProfile = findViewById(R.id.tab_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("플레이어");
                changeSubtitle(true);
                fragmentReplace(0);
            }
        });
        ImageButton btnNotice = findViewById(R.id.tab_notice);
        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("공지사항");
                tvSubTitle.setText("기록");
                fragmentReplace(1);
            }
        });
        ImageButton btnGame = findViewById(R.id.tab_game);
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("게임");
                if (currentGame == 0) { tvSubTitle.setText("마피아"); }
                else if (currentGame == 1) { tvSubTitle.setText("블록체인"); }
                fragmentReplace(2);
            }
        });
        ImageButton btnMessage = findViewById(R.id.tab_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("관리자");
                tvSubTitle.setText("메세지");
                fragmentReplace(3);
            }
        });
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
        Fragment newFragment = getFragment(reqNewFragmentIndex);
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag, newFragment);
        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        switch (idx) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new NoticeFragment();
            case 2:
                return new GameFragment();
                /*
                switch (currentGame) {
                    case 0:
                        return new GameMafiaFragment();
                    case 1:
                        return new GameBlockFragment();
                    default:
                        return null;
                }
                */
            case 3:
                return new MessageFragment();
            default:
                Log.d(TAG, "Unhandle case");
                return null;
        }
    }

    public void changeSubtitle(boolean mode) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1= new SpannableString("생존"+getString(R.string.tab)+getString(R.string.tab)+getString(R.string.tab));
        SpannableString str2= new SpannableString("사망");
        if (mode) {
            str1.setSpan(new ForegroundColorSpan(getColor(R.color.white)), 0, str1.length(), 0);
            builder.append(str1);
            str2.setSpan(new ForegroundColorSpan(getColor(R.color.txt_title)), 0, str2.length(), 0);
            builder.append(str2);
        } else {
            str1.setSpan(new ForegroundColorSpan(getColor(R.color.txt_title)), 0, str1.length(), 0);
            builder.append(str1);
            str2.setSpan(new ForegroundColorSpan(getColor(R.color.white)), 0, str2.length(), 0);
            builder.append(str2);

        }
        tvSubTitle.setText( builder, TextView.BufferType.SPANNABLE);
    }
}
