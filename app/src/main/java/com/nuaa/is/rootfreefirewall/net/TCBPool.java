package com.nuaa.is.rootfreefirewall.net;

import com.nuaa.is.rootfreefirewall.model.TCB;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TCB 存储池
 * @author John Kindem
 * @version v1.0
 */
public class TCBPool {

    // 线程安全的 hashMap 用于存储 TCBs
    private static ConcurrentHashMap<String, TCB> tcbPool;

    static {
        tcbPool = new ConcurrentHashMap<String, TCB>();
    }

    // 通过 ipAddress 和 port 获取 key
    private static String getKeyByIpAndPort(String ipAddress, int port) {
        String key = ipAddress + ":" + port;
        return key;
    }

    // 存储 TCB
    public static void putTCB(String ipAddress, int port, TCB tcb) {
        String key = TCBPool.getKeyByIpAndPort(ipAddress, port);
        if (key.isEmpty() || tcb == null) {
            return;
        }
        tcbPool.put(key, tcb);
    }

    // 获取 TCB
    public static TCB getTCB(String ipAddress, int port) {
        String key = TCBPool.getKeyByIpAndPort(ipAddress, port);
        return tcbPool.get(key);
    }

    // 关闭 TCB
    public static void closeTCB(String ipAddress, int port) {
        try {
            tcbPool
                    .get(TCBPool.getKeyByIpAndPort(ipAddress, port))
                    .getSelectionKey()
                    .channel()
                    .close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tcbPool.remove(ipAddress, port);
    }

    // 关闭所有 TCB
    public static void closeAllTCB() {
        Iterator iterator = tcbPool.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            TCB tcb = (TCB) entry.getValue();
            try {
                tcb.getSelectionKey().channel().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tcbPool.clear();
    }

}
