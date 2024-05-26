package com.qqb.maidshop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qqb.maidshop.common.BaseResponse;
import com.qqb.maidshop.common.ResultUtils;
import com.qqb.maidshop.model.Animation;
import com.qqb.maidshop.model.dto.AnimationRequestDTO;
import com.qqb.maidshop.model.dto.UserRequest;
import com.qqb.maidshop.service.AnimationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/animation")
public class AnimationController {

    @Resource
    private AnimationService animationService;

    @GetMapping("/list")
    public BaseResponse<PageInfo<Animation>> list(@RequestBody(required = false) UserRequest userRequest) {
        // 开启分页
        PageHelper.startPage(userRequest.getPageNum(), userRequest.getPageSize());
        List<Animation> list = animationService.list();
        PageInfo<Animation> pageInfo = new PageInfo<>(list);
        return ResultUtils.success(pageInfo);
    }

    @PostMapping("/insert")
    public BaseResponse<Integer> insert(@RequestBody AnimationRequestDTO animationRequestDTO) {
        Integer result = animationService.saveAnimation(animationRequestDTO);
        return ResultUtils.success(result);
    }
}
