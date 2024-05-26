package com.qqb.maidshop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 女仆
 * @TableName maid
 */
@TableName(value ="maid")
@Data
public class Maid implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     *  性别 0-女 1-男
     */
    private Integer gender;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 出处
     */
    private String provenance;

    /**
     * 头像
     */
    private String avatar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}