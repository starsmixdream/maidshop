package com.qqb.maidshop.service;

import com.qqb.maidshop.model.Animation;
import com.qqb.maidshop.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
* @author 22384
* @description 针对表【user】的数据库操作Service
* @createDate 2024-05-14 15:24:12
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param user
     */
    Integer registerUser(User user);

    /**
     * 用户登录
     * @param nickName
     * @param password
     * @return
     */
    User userLogin(String nickName, String password, HttpServletRequest request);

    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @return
     */
    User getCurrentLoginUser(HttpServletRequest request);

    List<User> selectUserByFavors(List<String> favors);

    /**
     * 当前无用户登录时，推荐热度最高的动漫
     * @return
     */
    Set<Animation> recommendNotLogin();

    /**
     * 根据登录用户的偏好推荐动漫
     * @return
     */
    Set<Animation> recommendByUserFavors(User user);

    /**
     * 获取当前登录用户，无论是否为空
     * @return
     */
    User getCurrentLoginUserIsOrNotNull(HttpServletRequest request);
}
