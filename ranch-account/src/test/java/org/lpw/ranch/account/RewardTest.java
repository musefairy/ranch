package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.tephra.util.TimeUnit;

/**
 * @author lpw
 */
public class RewardTest extends TestSupport {
    @Test
    public void reward() {
        validate("reward", 13);

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 1");
        mockHelper.getRequest().addParameter("owner", "owner 1");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("label", "label 1");
        mockHelper.mock("/account/reward");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(15, data.size());
        Assert.assertEquals("user 1", data.getString("user"));
        Assert.assertEquals("owner 1", data.getString("owner"));
        for (String property : new String[]{"type", "balance", "deposit", "withdraw", "reward", "profit", "consume", "remitIn", "remitOut", "refund"})
            Assert.assertEquals(0, data.getIntValue(property));
        Assert.assertEquals(1, data.getIntValue("pending"));
        AccountModel account = liteOrm.findById(AccountModel.class, data.getString("id"));
        Assert.assertEquals("user 1", account.getUser());
        Assert.assertEquals("owner 1", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getRefund());
        Assert.assertEquals(1, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 1&owner 1&0&0&0&0&0&0&0&0&0&0&1"), account.getChecksum());
        LogModel log = liteOrm.findById(LogModel.class, data.getString("logId"));
        Assert.assertEquals("user 1", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("owner 1", log.getOwner());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals(1, log.getAmount());
        Assert.assertEquals(0, log.getBalance());
        Assert.assertEquals(0, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() < 2000L);
        Assert.assertNull(log.getEnd());
        Assert.assertNull(log.getJson());

        thread.sleep(3, TimeUnit.Second);
        log.setAmount(2);
        liteOrm.save(log);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", log.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(0, object.getJSONArray("data").size());
        account = liteOrm.findById(AccountModel.class, account.getId());
        Assert.assertEquals("user 1", account.getUser());
        Assert.assertEquals("owner 1", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getRefund());
        Assert.assertEquals(1, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 1&owner 1&0&0&0&0&0&0&0&0&0&0&1"), account.getChecksum());
        log = liteOrm.findById(LogModel.class, log.getId());
        Assert.assertEquals("user 1", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals(2, log.getAmount());
        Assert.assertEquals(0, log.getBalance());
        Assert.assertEquals(0, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() > 2000L);
        Assert.assertNull(log.getEnd());
        Assert.assertNull(log.getJson());

        log.setAmount(1);
        liteOrm.save(log);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", log.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(1, object.getJSONArray("data").size());
        Assert.assertEquals(log.getId(), object.getJSONArray("data").getString(0));
        account = liteOrm.findById(AccountModel.class, account.getId());
        Assert.assertEquals("user 1", account.getUser());
        Assert.assertEquals("owner 1", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(1, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(1, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getRefund());
        Assert.assertEquals(0, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&user 1&owner 1&0&1&0&0&1&0&0&0&0&0&0"), account.getChecksum());
        log = liteOrm.findById(LogModel.class, log.getId());
        Assert.assertEquals("user 1", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals(1, log.getAmount());
        Assert.assertEquals(1, log.getBalance());
        Assert.assertEquals(1, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() > 2000L);
        Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() < 2000L);
        Assert.assertNull(log.getJson());

        mockUser();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("owner", "owner 2");
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("label", "label 2");
        mockHelper.mock("/account/reward");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(15, data.size());
        Assert.assertEquals("sign in id", data.getString("user"));
        Assert.assertEquals("owner 2", data.getString("owner"));
        for (String property : new String[]{"type", "balance", "deposit", "withdraw", "reward", "profit", "consume", "remitIn", "remitOut", "refund"})
            Assert.assertEquals(0, data.getIntValue(property));
        Assert.assertEquals(2, data.getIntValue("pending"));
        account = liteOrm.findById(AccountModel.class, data.getString("id"));
        Assert.assertEquals("sign in id", account.getUser());
        Assert.assertEquals("owner 2", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getRefund());
        Assert.assertEquals(2, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&sign in id&owner 2&0&0&0&0&0&0&0&0&0&0&2"), account.getChecksum());
        log = liteOrm.findById(LogModel.class, data.getString("logId"));
        Assert.assertEquals("sign in id", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("owner 2", log.getOwner());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals(2, log.getAmount());
        Assert.assertEquals(0, log.getBalance());
        Assert.assertEquals(0, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() < 2000L);
        Assert.assertNull(log.getEnd());
        Assert.assertNull(log.getJson());

        thread.sleep(3, TimeUnit.Second);
        String lockId = lockHelper.lock(AccountModel.NAME + ".service.lock:sign in id-owner 2-0", 1000L, 0);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", log.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/reject");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(0, object.getJSONArray("data").size());
        account = liteOrm.findById(AccountModel.class, account.getId());
        Assert.assertEquals("sign in id", account.getUser());
        Assert.assertEquals("owner 2", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getRefund());
        Assert.assertEquals(2, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&sign in id&owner 2&0&0&0&0&0&0&0&0&0&0&2"), account.getChecksum());
        log = liteOrm.findById(LogModel.class, data.getString("logId"));
        Assert.assertEquals("sign in id", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals(2, log.getAmount());
        Assert.assertEquals(0, log.getBalance());
        Assert.assertEquals(0, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() > 2000L);
        Assert.assertNull(log.getEnd());
        Assert.assertNull(log.getJson());
        lockHelper.unlock(lockId);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", log.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/reject");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(1, object.getJSONArray("data").size());
        Assert.assertEquals(log.getId(), object.getJSONArray("data").getString(0));
        account = liteOrm.findById(AccountModel.class, account.getId());
        Assert.assertEquals("sign in id", account.getUser());
        Assert.assertEquals("owner 2", account.getOwner());
        Assert.assertEquals(0, account.getType());
        Assert.assertEquals(0, account.getBalance());
        Assert.assertEquals(0, account.getDeposit());
        Assert.assertEquals(0, account.getWithdraw());
        Assert.assertEquals(0, account.getReward());
        Assert.assertEquals(0, account.getProfit());
        Assert.assertEquals(0, account.getConsume());
        Assert.assertEquals(0, account.getRemitIn());
        Assert.assertEquals(0, account.getRemitOut());
        Assert.assertEquals(0, account.getRefund());
        Assert.assertEquals(0, account.getPending());
        Assert.assertEquals(digest.md5(AccountModel.NAME + ".service.checksum&sign in id&owner 2&0&0&0&0&0&0&0&0&0&0&0"), account.getChecksum());
        log = liteOrm.findById(LogModel.class, data.getString("logId"));
        Assert.assertEquals("sign in id", log.getUser());
        Assert.assertEquals(account.getId(), log.getAccount());
        Assert.assertEquals("reward", log.getType());
        Assert.assertEquals(2, log.getAmount());
        Assert.assertEquals(0, log.getBalance());
        Assert.assertEquals(2, log.getState());
        Assert.assertTrue(System.currentTimeMillis() - log.getStart().getTime() > 2000L);
        Assert.assertTrue(System.currentTimeMillis() - log.getEnd().getTime() < 2000L);
        Assert.assertNull(log.getJson());
    }
}
