package com.zzc.utils.redis;

/**
 * @author Administrator
 */
public interface RedisKeyConstants {

    /**
     * 市场配置的社区URL地址key值
     */
    String COMMUNITY_ENTITY_URL = "communityUrl:%s";

    public static final String WEIXIN_ACCESS_TOKEN = "gcs.wx.purchase.access_token";

    /**
     * 从店铺地址生成收货地址key
     */
    String MAKE_ADDRESS_FROM_STORE_ADDRESS = "gcs.make_address.from_store_address_";

    /**
     * 市场代客下单-创建客户-授权码
     * 格式: 授权码
     */
    String MARKET_VALET_CREATE_CUSTOMER_AUTH_CODE = "gcs:market_valet:create_customer_auth_code:";

    /**
     * 市场代客下单-创建客户-授权码-redis锁
     */
    String MARKET_VALET_CREATE_CUSTOMER_AUTH_CODE_LOCK = "gcs.market_valet.create_customer_auth_code_lock";

    /**
     * 店铺员工邀请码 redis的Key
     */
    String INVITE_EMPLOYEE_CODE_KEY = "inviteEmpCode:%s:%s";

    static String inviteEmployeeCodeKey(final String adminId, final String timeStamp) {
        return String.format(INVITE_EMPLOYEE_CODE_KEY, adminId, timeStamp);
    }

    static String communityUrl(final String shopId) {
        return String.format(COMMUNITY_ENTITY_URL, shopId);
    }
}
