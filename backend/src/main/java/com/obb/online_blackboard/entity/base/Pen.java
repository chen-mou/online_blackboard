package com.obb.online_blackboard.entity.base;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 陈桢梁
 * @desc Pen.java
 * @date 2022-11-10 21:03
 * @logs[0] 2022-11-10 21:03 陈桢梁 创建了Pen.java文件
 */
@Data
@NoArgsConstructor
public class Pen {

    private double lineWidth;

    private String strokeStyle;

    private String fillStyle;

}