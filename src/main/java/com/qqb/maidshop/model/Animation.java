package com.qqb.maidshop.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 动漫
 * @TableName animation
 */
@TableName(value ="animation")
@Data
public class Animation implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
    private Date releaseTime;

    /**
     * 完结时间
     */
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除(0-正常 1-已删除)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}