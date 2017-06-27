package com.example.testroom;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testroom.dao.UserDao;
import com.example.testroom.database.UserDatabase;
import com.example.testroom.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";

    private RecyclerView recyclerView;
    private EditText editText;
    private TextView countTextView;

    private List<User> dataList = new ArrayList<>();
    private Adapter adapter;
    private UserDao userDao;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userDao = UserDatabase.getInstance(UserListActivity.this).getUserDao();

        editText = (EditText) findViewById(R.id.id_edit_text);
        countTextView = (TextView) findViewById(R.id.count_text_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new Adapter());

        showAllData();
    }

    public void deleteBtnClick(View view) {
        String content = editText.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            try {
                int id = Integer.parseInt(content);
                deleteById(id);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "输入的ID不合法", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteById(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setUid(id);
                int result = userDao.delete(user);
                dataList = userDao.selectAll();
                if (result == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    // 根据id查询记录
    public void searchBtnClick(View view) {
        String content = editText.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            try {
                int id = Integer.parseInt(content);
                searchById(id);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "输入的ID不合法", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void searchById(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = userDao.selectById(id);
                if (users != null) {
                    dataList.clear();
                    dataList.addAll(users);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    // 显示所有记录
    public void allBtnClick(View view) {
        showAllData();
    }

    private void showAllData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataList = userDao.selectAll();
                final int recordsCount = userDao.count();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countTextView.setText(String.format("数据库中共有%d条记录", recordsCount));
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(viewGroup);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.textView.setText(dataList.get(i).toString());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(UserListActivity.this).inflate(R.layout.list_item, parent, false));
                textView = (TextView) itemView.findViewById(R.id.list_item_text);
            }
        }
    }
}
