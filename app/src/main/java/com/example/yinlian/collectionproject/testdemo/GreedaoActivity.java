package com.example.yinlian.collectionproject.testdemo;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.library.base.BaseActivity;
import com.example.library.db.entity.User;
import com.example.library.db.gen.UserDao;
import com.example.library.db.helper.DbUtil;
import com.example.library.db.helper.UserHelper;
import com.example.library.utils.GlideUtils;
import com.example.yinlian.collectionproject.R;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class GreedaoActivity extends BaseActivity implements View.OnClickListener {

    private Button add;
    private Button delete;
    private Button updata;
    private Button addimage;
    private TextView textView;
    private ImageView image;
    private UserHelper userHelper;
    private List<User> users;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main3;
    }

    @Override
    public void init() {
        initTitle("22222");
        initToolbar(R.drawable.ic_back, R.menu.menu_main);
        userHelper = DbUtil.getUserHelper();
        add = findViewById(R.id.add);
        updata = findViewById(R.id.updata);
        delete = findViewById(R.id.delete);
        addimage = findViewById(R.id.addimage);
        textView = findViewById(R.id.text);
        addimage.setOnClickListener(this);
        image = findViewById(R.id.image);
        add.setOnClickListener(this);
        updata.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.addimage:
                GlideUtils.loadImageView(this, "https://www.baidu.com/img/bdlogo.png", image);
                break;
            case R.id.add:
                String s = "";


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
                String s1 = "";
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
                textView.setText("");
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                ToastUtils.showShort("click edit");
                break;
            case R.id.action_share:
                ToastUtils.showShort("click share");
                break;
            case R.id.action_overflow:
                break;
            default:
                break;
        }
        return true;
    }
}
