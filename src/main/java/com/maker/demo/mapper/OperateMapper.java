package com.maker.demo.mapper;

import com.maker.demo.entity.MessageReturn;
import com.maker.demo.entity.Operate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Maker
 * @since 2021-04-19
 */
public interface OperateMapper extends BaseMapper<Operate> {
    List<MessageReturn> getRecentMsg(int partner);
}
