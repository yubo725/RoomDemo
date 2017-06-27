package com.example.testroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.testroom.database.UserDatabase;
import com.example.testroom.entity.User;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private UserDatabase userDatabase;
    private Random random;
    private long year = 365 * 24 * 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDatabase = UserDatabase.getInstance(this);
        random = new Random();
    }

    public void handleBtnClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setName("test user " + random.nextInt(100));
                user.setGender(random.nextBoolean());
                user.setBirthday(System.currentTimeMillis() - random.nextInt(50) * year);
                user.setJob("teacher");
                userDatabase.getUserDao().insert(user);
            }
        }).start();
    }

    public void handleShowBtnClick(View view) {
        startActivity(new Intent(this, UserListActivity.class));
    }

}
