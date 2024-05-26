package com.qqb.maidshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qqb.maidshop.model.Animation;
import com.qqb.maidshop.model.dto.AnimationRequestDTO;
import com.qqb.maidshop.service.AnimationService;
import com.qqb.maidshop.mapper.AnimationMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 22384
* @description 针对表【animation(动漫)】的数据库操作Service实现
* @createDate 2024-05-21 23:20:38
*/
@Service
public class AnimationServiceImpl extends ServiceImpl<AnimationMapper, Animation>
    implements AnimationService{

    @Resource
    private AnimationMapper animationMapper;

    @Override
    public int saveAnimation(AnimationRequestDTO animationRequestDTO) {
        Animation animation = new Animation();
        BeanUtils.copyProperties(animationRequestDTO, animation);
        return animationMapper.insert(animation);
    }
}




