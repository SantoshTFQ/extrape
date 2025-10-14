package com.techlabs.extrape.utiles;

public class ApiUrls {

    // 🔹 Base URL (change this when you move server)
    public static final String BASE_URL = "http://192.168.0.200/linksaas/api/";
   // public static final String BASE_URL1 = "http://localhost/linksaas/api";

    // 🔹 API endpoints
    public static final String GET_USER_REELS = BASE_URL + "getUserReels.php";
    public static final String GENERATE_AFFILIATE = BASE_URL + "generateAffiliate.php";
    public static final String SAVE_REEL_SETUP = BASE_URL + "saveReelSetup.php";
    public static final String UPDATE_ANALYTICS = BASE_URL + "updateAnalytics.php";

    // 🔹 Optional future endpoints
    public static final String META_WEBHOOK = BASE_URL + "metaWebhook.php";
    public static final String REFRESH_REELS = BASE_URL + "refreshReels.php"; // (if added later)

    // 🔹 If you add user login or token
    public static final String LOGIN_USER = BASE_URL + "login.php";
    public static final String REGISTER_USER = BASE_URL + "register.php";
}
