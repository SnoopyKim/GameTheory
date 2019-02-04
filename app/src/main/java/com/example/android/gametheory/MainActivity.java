package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Switch;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = ".MainActivity";

    TextView tvTitle, tvSubTitle, tvTimer, tvTimerText;
    Switch switchStatus;

    RelativeLayout layout, rlTimer;

    ImageButton btnProfile, btnNotice, btnGame, btnMessage;

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

        layout = findViewById(R.id.rl_main_scene);
        tvTitle = findViewById(R.id.tv_title);
        tvSubTitle = findViewById(R.id.tv_subtitle);
        tvTimer = findViewById(R.id.tv_timer);
        tvTimerText = findViewById(R.id.tv_timer_text);
        switchStatus = findViewById(R.id.switch_status);
        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeSubtitle(!isChecked);
                try {
                    PlayerAdapter adapter = ((ProfileFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.frag))).playerAdapter;
                    adapter.sortAlive(!isChecked);
                } catch (ClassCastException ce) {
                    ce.printStackTrace();
                }
            }
        });

        ImageButton ibMenu = findViewById(R.id.ib_menu);
        final ListPopupWindow popupWindow = new ListPopupWindow(this);
        List<HashMap<String,String>> data = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("status", getString(R.string.show_alive));
        data.add(map);
        map = new HashMap<>();
        map.put("status", getString(R.string.show_dead));
        data.add(map);
        ListAdapter adapter = new SimpleAdapter(this, data, R.layout.simple_menu_view, new String[] {"status"}, new int[] {R.id.tv_menu});
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(280);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayerAdapter adapter = ((ProfileFragment)Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.frag))).playerAdapter;
                if (position==0) {
                    changeSubtitle(true);
                    adapter.sortAlive(true);
                } else {
                    changeSubtitle(false);
                    adapter.sortAlive(false);
                }
                popupWindow.dismiss();
            }
        });
        ibMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setAnchorView(v);
                popupWindow.show();
            }
        });

        rlTimer = findViewById(R.id.rl_timer);
        rlTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blurry.delete(layout);
                rlTimer.setVisibility(View.GONE);
            }
        });
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Blurry.with(getApplicationContext()).animate(500).onto(layout);

                rlTimer.setVisibility(View.VISIBLE);
                DifferentColorCircularBorder border = new DifferentColorCircularBorder(rlTimer);
                border.addBorderPortion(getApplicationContext(), getColor(R.color.point_on), -90, 10);
                border.addBorderPortion(getApplicationContext(), getColor(R.color.point_off), 10, 190);
                border.addBorderPortion(getApplicationContext(), getColor(R.color.transparent), 190, 270);
            }
        });

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
                        tvTimerText.setText(String.format("%02d:%02d:00", minute,second));
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

        btnProfile = findViewById(R.id.tab_profile);
        btnProfile.setSelected(true);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()) {
                    btnNotice.setSelected(false);
                    btnGame.setSelected(false);
                    btnMessage.setSelected(false);
                } else { return; }
                v.setSelected(!v.isSelected());
                tvTitle.setText("플레이어");
                changeSubtitle(true);
                fragmentReplace(0);
            }
        });
        btnNotice = findViewById(R.id.tab_notice);
        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()) {
                    btnProfile.setSelected(false);
                    btnGame.setSelected(false);
                    btnMessage.setSelected(false);
                } else { return; }
                v.setSelected(!v.isSelected());
                tvTitle.setText("공지사항");
                tvSubTitle.setText("기록");
                fragmentReplace(1);
            }
        });
        btnGame = findViewById(R.id.tab_game);
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()) {
                    btnNotice.setSelected(false);
                    btnProfile.setSelected(false);
                    btnMessage.setSelected(false);
                } else { return; }
                v.setSelected(!v.isSelected());
                tvTitle.setText("게임");
                if (currentGame == 0) { tvSubTitle.setText("마피아"); }
                else if (currentGame == 1) { tvSubTitle.setText("블록체인"); }
                fragmentReplace(2);
            }
        });
        btnMessage = findViewById(R.id.tab_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isSelected()) {
                    btnNotice.setSelected(false);
                    btnGame.setSelected(false);
                    btnProfile.setSelected(false);
                } else { return; }
                v.setSelected(!v.isSelected());
                tvTitle.setText("관리자");
                tvSubTitle.setText("메세지");
                fragmentReplace(3);
            }
        });
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
        if (reqNewFragmentIndex == 0) {
            switchStatus.setVisibility(View.VISIBLE);
            switchStatus.setChecked(false);
            findViewById(R.id.view).setVisibility(View.GONE);
        } else {
            switchStatus.setVisibility(View.GONE);
            findViewById(R.id.view).setVisibility(View.VISIBLE);
        }

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
            tvSubTitle.setBackground(getDrawable(R.drawable.bg_timer_txt));
            str1.setSpan(new ForegroundColorSpan(getColor(R.color.white)), 0, str1.length(), 0);
            builder.append(str1);
            str2.setSpan(new ForegroundColorSpan(getColor(R.color.txt_title)), 0, str2.length(), 0);
            builder.append(str2);
        } else {
            tvSubTitle.setBackground(getDrawable(R.drawable.bg_timer_txt_off));
            str1.setSpan(new ForegroundColorSpan(getColor(R.color.txt_title)), 0, str1.length(), 0);
            builder.append(str1);
            str2.setSpan(new ForegroundColorSpan(getColor(R.color.white)), 0, str2.length(), 0);
            builder.append(str2);
        }
        tvSubTitle.setText( builder, TextView.BufferType.SPANNABLE);
    }

}
