package com.maker.demo.service;

import com.maker.demo.entity.MessageReturn;
import com.maker.demo.entity.Operate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Maker
 * @since 2021-04-19
 */
public interface OperateService extends IService<Operate> {
    List<MessageReturn> getRecentMsg(int partner);

}
