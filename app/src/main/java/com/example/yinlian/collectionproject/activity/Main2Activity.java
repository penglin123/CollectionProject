package com.example.yinlian.collectionproject.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.app.BaseTopBarActivity;
import com.example.yinlian.collectionproject.db.entity.User;
import com.example.yinlian.collectionproject.db.gen.UserDao;
import com.example.yinlian.collectionproject.db.helper.DbUtil;
import com.example.yinlian.collectionproject.db.helper.UserHelper;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * @author penglin
 * @date 2018/4/10
 */

public class Main2Activity extends BaseTopBarActivity implements View.OnClickListener {
    private Button add;
    private Button delete;
    private Button updata;
    private TextView textView;
    private UserHelper userHelper;
    private List<User> users;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void init() {
        userHelper = DbUtil.getUserHelper();
        add = findViewById(R.id.add);
        updata = findViewById(R.id.updata);
        delete = findViewById(R.id.delete);
        textView = findViewById(R.id.text);
        add.setOnClickListener(this);
        updata.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add:
                String s="";
                User user = new User();
                user.setName("zhangsan");
                userHelper.save(user);
                users = userHelper.queryAll();
                textView.setText("");
                for (int i = 0; i < users.size(); i++) {
                    s = s + users.get(i).getName() + "\n";
                }
                textView.setText(s);
                break;
            case R.id.updata:
                String s1="";
                Query<User> zhangsan = userHelper.queryBuilder().where(UserDao.Properties.Name.eq("zhangsan")).build();

                List<User> list = zhangsan.list();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setName("lisi");
                }

                userHelper.update(list);
                 users = userHelper.queryAll();
                textView.setText("");
                for (int i = 0; i < users.size(); i++) {
                    s1 = s1 + users.get(i).getName() + "\n";
                }
                textView.setText(s1);
                break;
            case R.id.delete:
                userHelper.deleteAll();
                break;
        }
    }
}
