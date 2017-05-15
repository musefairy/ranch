package org.lpw.ranch.account;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AccountModel.NAME + ".dao")
class AccountDaoImpl implements AccountDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<AccountModel> query(String user) {
        return liteOrm.query(new LiteQuery(AccountModel.class).where("c_user=?").order("c_type"), new Object[]{user});
    }

    @Override
    public PageList<AccountModel> query(String user, String owner) {
        return liteOrm.query(new LiteQuery(AccountModel.class).where("c_user=? and c_owner=?").order("c_type"), new Object[]{user, owner});
    }

    @Override
    public PageList<AccountModel> query(String user, int pageSize, int pageNum) {
        return validator.isEmpty(user) ? liteOrm.query(new LiteQuery(AccountModel.class).order("c_balance desc").size(pageSize).page(pageNum), null) :
                liteOrm.query(new LiteQuery(AccountModel.class).where("c_user=?").order("c_balance desc").size(pageSize).page(pageNum), new Object[]{user});
    }

    @Override
    public AccountModel findById(String id) {
        return liteOrm.findById(AccountModel.class, id);
    }

    @Override
    public AccountModel find(String user, String owner, int type) {
        return liteOrm.findOne(new LiteQuery(AccountModel.class).where("c_user=? and c_owner=? and c_type=?"), new Object[]{user, owner, type});
    }

    @Override
    public void save(AccountModel account) {
        liteOrm.save(account);
    }
}
