package com.techlabs.extrape.utiles;

public class ApiUrls {

    // 🔹 Base URL (change this when you move server)
    public static final String BASE_URL2 = "http://192.168.0.200/linksaas/api/";
    public static final String BASE_URL3 = "http://10.0.2.2/linksaas/api";
    public static final String BASE_URL = "https://iot.santoshtech.com/linksaas/api/";


    // 🔹 API endpoints
    public static final String GET_USER_REELS = BASE_URL + "getUserReels.php";
    public static final String GENERATE_AFFILIATE = BASE_URL + "generateAffiliate.php";
    public static final String SAVE_REEL_SETUP = BASE_URL + "saveReelSetup.php";
    public static final String GET_REEL_SETUP = BASE_URL + "getReelSetup.php";
    public static final String GET_USER_ANALYTICS = BASE_URL + "getUserAnalytics.php";
    public static final String GET_USER_EARNINGS = BASE_URL + "getUserEarnings.php";
    public static final String REQUEST_WITHDRAW = BASE_URL + "requestWithdraw.php";

    public static final String UPDATE_ANALYTICS = BASE_URL + "updateAnalytics.php";
    public static final String GET_EARN_HISTORY = BASE_URL + "getEarningHistory.php";
    /*
    * 🔹 If you add user login or token*/
    public static final String SYNC_FIREBASE_USER = BASE_URL + "auth/syncFirebaseUser.php";
    public static final String REGISTER_USER = BASE_URL + "auth/registerUser.php";
    public static final String LOGIN_USER = BASE_URL + "auth/loginUser.php";


    // 🔹 Optional future endpoints
    public static final String META_WEBHOOK = BASE_URL + "metaWebhook.php";
    public static final String REFRESH_REELS = BASE_URL + "refreshReels.php"; // (if added later)

    // 🔹 If you add user login or token
    //public static final String LOGIN_USER = BASE_URL + "login.php";
    //public static final String REGISTER_USER = BASE_URL + "register.php";
}