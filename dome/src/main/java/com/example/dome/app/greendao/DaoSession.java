package com.example.dome.app.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.dome.dbbean.UesrLogin;

import com.example.dome.app.greendao.UesrLoginDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig uesrLoginDaoConfig;

    private final UesrLoginDao uesrLoginDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        uesrLoginDaoConfig = daoConfigMap.get(UesrLoginDao.class).clone();
        uesrLoginDaoConfig.initIdentityScope(type);

        uesrLoginDao = new UesrLoginDao(uesrLoginDaoConfig, this);

        registerDao(UesrLogin.class, uesrLoginDao);
    }
    
    public void clear() {
        uesrLoginDaoConfig.clearIdentityScope();
    }

    public UesrLoginDao getUesrLoginDao() {
        return uesrLoginDao;
    }

}
