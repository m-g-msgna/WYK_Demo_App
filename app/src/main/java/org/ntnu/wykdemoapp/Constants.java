package org.ntnu.wykdemoapp;

public class Constants {
    public interface WYK_SERVER {
        public static String IP = "http://10.24.100.190:9090";      //This is the ip of my laptop
        public static String[] ENDPOINTS = {
                "/wyk/api/v1/user/info?uid="
        };
    }

    public interface DATA_TYPE {
        public static int AUTH_LOG = 0;
        public static int HASH_LOG = 1;
    }
}
