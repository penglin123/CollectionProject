package com.example.yinlian.collectionproject.db.helper;

import com.example.yinlian.collectionproject.db.entity.User;
import org.greenrobot.greendao.AbstractDao;

/**
 * Created by penglin on 2018/4/12.
 */
public class UserHelper extends BaseDbHelper<User, Long> {

    public UserHelper(AbstractDao dao) {
        super(dao);
    }
}
