package com.tanhua.dubbo.api;

import com.tanhua.domain.db.User;

/**
 * 用户服务接口
 */
public interface UserApi {
    /**
     * 保存用户
     */
    Long save(User user);

    /**
     * 根据手机号码查询
     */
    User findByMobile(String mobile);
}
