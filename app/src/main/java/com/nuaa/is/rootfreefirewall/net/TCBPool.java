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
    private static String getKeyByIpAndPort(
            String destIpAddress,
            int sourcePort,
            int destPort
    ) {
        String key = sourcePort + "-" + destIpAddress + ":" + destPort;
        return key;
    }

    // 存储 TCB
    public static void putTCB(
            int sourcePort,
            String destIpAddress,
            int destPort,
            TCB tcb
    ) {
        String key = TCBPool.getKeyByIpAndPort(destIpAddress, sourcePort, destPort);
        if (key.isEmpty() || tcb == null) {
            return;
        }
        tcbPool.put(key, tcb);
    }

    // 获取 TCB
    public static TCB getTCB(
            int sourcePort,
            String destIpAddress,
            int destPort
    ) {
        String key = TCBPool.getKeyByIpAndPort(destIpAddress, sourcePort, destPort);
        return tcbPool.get(key);
    }

    // 关闭 TCB
    public static void closeTCB(
            int sourcePort,
            String destIpAddress,
            int destPort
    ) {
        String key = TCBPool.getKeyByIpAndPort(destIpAddress, sourcePort, destPort);
        try {
            tcbPool
                    .get(key)
                    .getSelectionKey()
                    .channel()
                    .close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tcbPool.remove(key);
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