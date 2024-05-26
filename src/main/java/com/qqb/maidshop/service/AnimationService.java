package com.qqb.maidshop.service;

import com.qqb.maidshop.model.Animation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qqb.maidshop.model.dto.AnimationRequestDTO;

/**
* @author 22384
* @description 针对表【animation(动漫)】的数据库操作Service
* @createDate 2024-05-21 23:20:38
*/
public interface AnimationService extends IService<Animation> {

    /**
     * 保存动漫
     *
     * @param animationRequestDTO
     * @return
     */
    int saveAnimation(AnimationRequestDTO animationRequestDTO);
}
