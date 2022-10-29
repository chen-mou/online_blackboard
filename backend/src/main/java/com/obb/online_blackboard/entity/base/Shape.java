package com.obb.online_blackboard.entity.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author 陈桢梁
 * @desc Shape.java
 * @date 2022-10-27 09:12
 * @logs[0] 2022-10-27 09:12 陈桢梁 创建了Shape.java文件
 */
@Data
@RedisHash("Shape")
@NoArgsConstructor
public abstract class Shape extends Date{

    @Id
    private long id;

    protected String type;

    protected long sheetId;

    protected String color;

    protected int index;

    protected int x;

    protected int y;

    public Shape(Shape s){
        this.type = s.type;
        this.sheetId = s.sheetId;
        this.color = s.color;
        this.index = s.index;
        this.x = s.x;
        this.y = s.y;
    }

    public abstract void setLineWidth(int lineWidth);

    public abstract void setColor(String color);

}
