package com.cxytiandi.kitty.common.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author: yinjihuan
 * @create: 2020-02-01 23:05
 */
public class CatTransactionManager {

    public static <T> T newTransaction(Supplier<T> function, String type, String name, Map<String, Object> data) throws Exception {
        Transaction transaction = Cat.newTransaction(type, name);
        if (data != null && !data.isEmpty()) {
            data.forEach(transaction::addData);
        }
        try {
            T result = function.get();
            transaction.setStatus(Message.SUCCESS);
            return result;
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Cat.logEvent(type + "_" + name + "_Error", e.getMessage());
            }
            transaction.setStatus(e);
            throw e;
        } finally {
            transaction.complete();
        }
    }

    public static <T> T newTransaction(Supplier<T> function, String type, String name) throws Exception {
        return newTransaction(function, type, name, null);
    }

}