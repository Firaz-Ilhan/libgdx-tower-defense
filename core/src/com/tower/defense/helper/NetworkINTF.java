package com.tower.defense.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Only static access allowed
 */
public final class NetworkINTF {

    private final static Logger log = LogManager.getLogger(NetworkINTF.class);

    //prevents the class from being instantiated
    private NetworkINTF() {
    }

    /**
     * used to show own IP address in Matchmaking screen
     * @return
     * @throws UnknownHostException
     */

    public static String getLocalIpAddress() throws UnknownHostException {
        final String localIp = InetAddress.getLocalHost().getHostAddress();
        log.info("local ip address: {}", localIp);
        return localIp;
    }

    public static String getHostName() throws UnknownHostException {
        final String hostName = InetAddress.getLocalHost().getHostName();
        log.info("host name: {}", hostName);
        return hostName;
    }
}
