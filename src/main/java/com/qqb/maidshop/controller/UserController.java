package com.qqb.maidshop.controller;

import com.qqb.maidshop.common.BaseResponse;
import com.qqb.maidshop.common.ErrorCode;
import com.qqb.maidshop.common.ResultUtils;
import com.qqb.maidshop.constant.Constant;
import com.qqb.maidshop.exception.BusinessException;
import com.qqb.maidshop.model.Animation;
import com.qqb.maidshop.model.User;
import com.qqb.maidshop.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestParam String nickname, @RequestParam String password, HttpServletRequest request) {
        User user = userService.userLogin(nickname, password, request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
//        return new BaseResponse(0,user);
        return ResultUtils.success(user);
    }

    @GetMapping("/getCurrentLoginUser")
    public BaseResponse<User> getCurrentLoginUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getCurrentLoginUser(request));
    }

    @PostMapping("/logout")
    public Boolean userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(Constant.LOGIN_USER);
        return true;
    }

    @GetMapping("/search/byFavors")
    public BaseResponse<List<User>> searchUserByFavors(@RequestParam(required = false) List<String> favors) {
        if (CollectionUtils.isEmpty(favors)) {
            throw new BusinessException(ErrorCode.NULL_PARAMS);
        }
        return ResultUtils.success(userService.selectUserByFavors(favors));
    }

    @GetMapping("/recommend")
    public BaseResponse<Set<Animation>> recommend(HttpServletRequest request) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        User user = userService.getCurrentLoginUserIsOrNotNull(request);
        if (user == null) {
            // 当前无用户登录，推荐热度最高的动漫
            // 1.缓存存在则返回缓存
            // 2.不存在则查数据库，并写入缓存
            Set<Animation> recommendAnimation = (Set<Animation>) valueOperations.get(Constant.RECOMMEND_ANIMATION_NOTLOGIN);
            if (CollectionUtils.isEmpty(recommendAnimation)) {
                // 缓存为空，查数据库并写入缓存
                Set<Animation> animations = userService.recommendNotLogin();
                valueOperations.set(Constant.RECOMMEND_ANIMATION_NOTLOGIN, animations, 30000, TimeUnit.MILLISECONDS);
                return ResultUtils.success(animations);
            }
            return ResultUtils.success(recommendAnimation);
        }
        // 否则根据用户偏好推荐动漫
        // 查缓存
        // 1.缓存存在则返回缓存
        Set<Animation> recommendAnimation = (Set<Animation>) valueOperations.get(Constant.RECOMMEND_ANIMATION_USER + user.getId());
        // 2.不存在则查数据库，并写入缓存
        if (CollectionUtils.isEmpty(recommendAnimation)) {
            Set<Animation> animations = userService.recommendByUserFavors(user);
            valueOperations.set(Constant.RECOMMEND_ANIMATION_USER + user.getId(), animations, 3600, TimeUnit.MINUTES);
            return ResultUtils.success(animations);
        }
        return ResultUtils.success(recommendAnimation);
    }

}
