package com.globalformulae.shiguang.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.globalformulae.shiguang.model.Schedule;
import com.globalformulae.shiguang.model.Subject;

import com.globalformulae.shiguang.greendao.ScheduleDao;
import com.globalformulae.shiguang.greendao.SubjectDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig scheduleDaoConfig;
    private final DaoConfig subjectDaoConfig;

    private final ScheduleDao scheduleDao;
    private final SubjectDao subjectDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        scheduleDaoConfig = daoConfigMap.get(ScheduleDao.class).clone();
        scheduleDaoConfig.initIdentityScope(type);

        subjectDaoConfig = daoConfigMap.get(SubjectDao.class).clone();
        subjectDaoConfig.initIdentityScope(type);

        scheduleDao = new ScheduleDao(scheduleDaoConfig, this);
        subjectDao = new SubjectDao(subjectDaoConfig, this);

        registerDao(Schedule.class, scheduleDao);
        registerDao(Subject.class, subjectDao);
    }
    
    public void clear() {
        scheduleDaoConfig.clearIdentityScope();
        subjectDaoConfig.clearIdentityScope();
    }

    public ScheduleDao getScheduleDao() {
        return scheduleDao;
    }

    public SubjectDao getSubjectDao() {
        return subjectDao;
    }

}
