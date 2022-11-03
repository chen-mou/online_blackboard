package com.obb.online_blackboard.model;

import com.obb.online_blackboard.dao.redis.OperateDao;
import com.obb.online_blackboard.entity.base.Operate;
import org.springframework.stereotype.Repository;
import tool.util.id.Id;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author 陈桢梁
 * @desc OperateModel.java
 * @date 2022-11-03 19:02
 * @logs[0] 2022-11-03 19:02 陈桢梁 创建了OperateModel.java文件
 */
@Repository
public class OperateModel {

    @Resource
    OperateDao operateDao;

    @Resource
    Id id;

    public Operate createOperate(Operate op){
        long id = this.id.getId("operate");
        op.setId(id);
        operateDao.save(op);
        return op;
    }

    public Operate getById(long id){
        Optional<Operate> op = operateDao.findById(id);
        if(op.isEmpty()){
            return null;
        }
        return op.get();
    }

    public void delete(long id){
        operateDao.deleteById(id);
    }

}
