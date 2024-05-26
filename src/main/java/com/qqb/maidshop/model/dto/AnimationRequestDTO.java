package com.qqb.maidshop.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimationRequestDTO implements Serializable {

    /**
     * 番名
     */
    private String name;

    /**
     * 标签
     */
    private String tag;

    /**
     * 播出时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseTime;

    /**
     * 完结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 状态 0-已完结 1-更新中 2-未播出
     */
    private Integer status;

    /**
     * 集数
     */
    private Integer numberOfEpisode;

    /**
     * 简介
     */
    private String intro;

    /**
     * 封面
     */
    private String cover;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
