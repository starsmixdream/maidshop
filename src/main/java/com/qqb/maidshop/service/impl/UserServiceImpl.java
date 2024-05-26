package com.qqb.maidshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qqb.maidshop.common.ErrorCode;
import com.qqb.maidshop.constant.Constant;
import com.qqb.maidshop.exception.BusinessException;
import com.qqb.maidshop.mapper.AnimationMapper;
import com.qqb.maidshop.model.Animation;
import com.qqb.maidshop.model.User;
import com.qqb.maidshop.service.UserService;
import com.qqb.maidshop.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 22384
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-05-14 15:24:12
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Value("${animation.recommend-num}")
    private int recommendNum;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AnimationMapper animationMapper;

    @Override
    public Integer registerUser(User user) {
        // 验证为空
        String nickname = user.getNickname();
        String password = user.getPassword();
        if (StringUtils.isAnyBlank(nickname, password)) {
            return null;
        };
        // 加密密码
        MessageDigest md5 = null;
        String newPassword;
        try {
            md5 = MessageDigest.getInstance("MD5");
            Base64.Encoder base64Encoder = Base64.getEncoder();
            // 加密字符串--在此处添加密码盐
            newPassword = (base64Encoder.encodeToString(md5.digest(password.getBytes("utf-8"))) + Constant.ACC_SALT).replace("=", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        user.setPassword(newPassword);
        // 存数据库
        userMapper.insert(user);
        return 1;
    }

    @Override
    public User userLogin(String nickName, String password, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(nickName, password)) {
            return null;
        }
        // todo 此时后端做加密，前端传输的依然是明文密码
        MessageDigest md5 = null;
        String newPassword;
        try {
            md5 = MessageDigest.getInstance("MD5");
            Base64.Encoder base64Encoder = Base64.getEncoder();
            // 加密字符串--在此处添加密码盐
            newPassword = (base64Encoder.encodeToString(md5.digest(password.getBytes("utf-8"))) + Constant.ACC_SALT).replace("=", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nickname",nickName);
        queryWrapper.eq("password",newPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        // 登陆成功后将用户登录信息存入session
        request.getSession().setAttribute(Constant.LOGIN_USER,user);
        return safeUserInfo(user);
    }

    /**
     * 获取当前登录用户信息,返回脱敏后的用户信息
     *
     * @param request
     * @return
     */
    @Override
    public User getCurrentLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.LOGIN_USER);
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return safeUserInfo(user);
    }

    /**
     * 根据偏好筛选用户(sql 查询)
     * @param favors
     * @return
     */
    @Override
    public List<User> selectUserByFavors(List<String> favors) {
        if (CollectionUtils.isEmpty(favors)) {
            throw new BusinessException(ErrorCode.NULL_PARAMS);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String favor : favors) {
            queryWrapper.like("favor",favor);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        return users.stream().map(this::safeUserInfo).collect(Collectors.toList());
//        return searchUserByFavors(favors);
    }

    @Override
    public Set<Animation> recommendNotLogin() {
        // todo 按照热度推荐动漫
        // 简化版 推荐前10部动漫
        QueryWrapper<Animation> queryWrapper = new QueryWrapper<>();
        // 限制查10条
        queryWrapper.last("limit " + recommendNum);
        List<Animation> animations = animationMapper.selectList(queryWrapper);
        return new HashSet<>(animations);
    }

    @Override
    public Set<Animation> recommendByUserFavors(User user) {
        // todo 根据算法和技术可优化
        // todo 重复的问题
        // 获取登录用户的偏好
        String favor = user.getFavor();
        if (StringUtils.isBlank(favor)) {
            // 如果用户无偏好，则按无登录推荐动漫
            return recommendNotLogin();
        }
        // 切割用户偏好为字符串数组
        String[] favors = favor.split(",");
        // 按用户偏好推荐10部动漫(根据偏好数量比例推荐)
        int size = favors.length;
        // todo 推荐的10部应写在配置文件中，待优化
        int avg = recommendNum / size;
        List<Animation> recommendAnimation = new ArrayList<>();
        // avg为每一类型推荐的动漫数量
        for (int i = 0; i < size; i++) {
            QueryWrapper<Animation> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("tag", favors[i]);
            if (i == size - 1) {
                //当前为最后一种类型推荐，需要在avg的基础上补齐余数
                avg = avg + recommendNum % size;
                queryWrapper.last("limit " + avg);
                List<Animation> animations = animationMapper.selectList(queryWrapper);
                recommendAnimation.addAll(animations);
                return new HashSet<>(completeRepeatAnimation(i, avg, favors[i], new HashSet<>(recommendAnimation)));
            }
            queryWrapper.last("limit " + avg);
            List<Animation> animations = animationMapper.selectList(queryWrapper);
            recommendAnimation.addAll(animations);
            // 自动补全重复的数据
            Set<Animation> animationsSet = completeRepeatAnimation(i, avg, favors[i], new HashSet<>(recommendAnimation));
            recommendAnimation = new ArrayList<>(animationsSet);
        }
        return new HashSet<>(completeRecommend(recommendAnimation));
    }

    /**
     * 补全因标签相同而导致重复的动漫
     * @return
     */
    private Set<Animation> completeRepeatAnimation(int i, int avg,String tag, Set<Animation> recommendAnimations) {
        // 补全重复的
        int lack = (i + 1) * avg - recommendAnimations.size();
        // 如果有缺失的，查最后几条数据补全
        if (lack > 0) {
            // i + 1
            QueryWrapper<Animation> queryWrapper = new QueryWrapper<>();
            queryWrapper.last(" ORDER BY id DESC limit " + lack);
            List<Animation> animations = animationMapper.selectList(queryWrapper);
            recommendAnimations.addAll(animations);
        }
        // 如果最后的数据也和前面的重复，则查其他的数据补全
        while (recommendAnimations.size() < (i + 1) * avg) {
            QueryWrapper<Animation> queryWrapper = new QueryWrapper<>();
            queryWrapper.notLike("tag", tag);
            List<Animation> animations = animationMapper.selectList(queryWrapper);
            recommendAnimations.addAll(animations);
        }
        return recommendAnimations;
    }

    @Override
    public User getCurrentLoginUserIsOrNotNull(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.LOGIN_USER);
        return safeUserInfo(user);
    }

    private User safeUserInfo(User user) {
        if (user == null) {
            return null;
        }
        User safetyUser = User.builder()
                .nickname(user.getNickname())
                .gender(user.getGender())
                .favor(user.getFavor())
                .id(user.getId())
                .build();
        return safetyUser;
    }

    /**
     * 根据偏好查询用户(本地内存)
     * @param favors
     * @return
     */
    @Deprecated
    private List<User> searchUserByFavors(List<String> favors) {
        if (CollectionUtils.isEmpty(favors)) {
            throw new BusinessException(ErrorCode.NULL_PARAMS);
        }
        // 1.查出全部用户 todo 以下写法不是很理解
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2.在内存中判断是否包含要求的偏好
        return users.stream().filter(user -> {
            String favor = user.getFavor();
            if (StringUtils.isBlank(favor)) {
                return false;
            }
            Set<String> templeFavorsSet = gson.fromJson(favor, new TypeToken<Set<String>>() {
            }.getType());
            // if语句的其他写法
            templeFavorsSet= Optional.ofNullable(templeFavorsSet).orElse(new HashSet<>());
            for (String f : favors) {
                if (!templeFavorsSet.contains(f)) {
                    return false;
                }
            }
            return true;
        }).map(this::safeUserInfo).collect(Collectors.toList());
    }

    // 当推荐视频条数不满足要求时，自动补全
    private List<Animation> completeRecommend(List<Animation> list) {
        if (list.size() < recommendNum) {
            QueryWrapper<Animation> queryWrapper = new QueryWrapper<>();
            queryWrapper.last("limit " + (recommendNum - list.size()));
            List<Animation> animations = animationMapper.selectList(queryWrapper);
            list.addAll(animations);
            return list;
        }
        return list;
    }
}




