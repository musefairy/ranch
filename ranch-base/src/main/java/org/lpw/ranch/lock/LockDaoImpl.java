package org.lpw.ranch.lock;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(LockModel.NAME + ".dao")
class LockDaoImpl implements LockDao {
    @Inject
    private LiteOrm liteOrm;
    @Value("${ranch.lock.data-source.key:}")
    private String dataSource;

    @Override
    public LockModel findByMd5(String md5) {
        return liteOrm.findOne(new LiteQuery(LockModel.class).dataSource(dataSource).where("c_md5=?").order("c_index"), new Object[]{md5});
    }

    @Override
    public void save(LockModel lock) {
        liteOrm.save(dataSource, lock);
        liteOrm.close();
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(dataSource, LockModel.class, id);
        liteOrm.close();
    }

    @Override
    public void delete(long expire) {
        liteOrm.delete(new LiteQuery(LockModel.class).where("c_expire<?"), new Object[]{expire});
        liteOrm.close();
    }
}
