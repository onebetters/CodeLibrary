package com.zzc.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

/**
 * @author Administrator
 */
@Slf4j
@UtilityClass
public class ServerMetaUtils {

    private final static String SERVER_IP;
    private final static int    PID;

    static {
        String ip = "127.0.0.1";
        int pid = -1;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            if (null != jvmName) {
                pid = Integer.parseInt(jvmName.substring(0, jvmName.indexOf('@')));
            }
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
        SERVER_IP = ip;
        PID = pid;
    }

    public String getLocalIp() {
        return SERVER_IP;
    }

    public int getPid() {
        return PID;
    }
}