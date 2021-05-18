package com.maker.demo.service.impl;

import com.maker.demo.entity.MessageReturn;
import com.maker.demo.entity.Operate;
import com.maker.demo.mapper.OperateMapper;
import com.maker.demo.service.OperateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Maker
 * @since 2021-04-19
 */
@Service
public class OperateServiceImpl extends ServiceImpl<OperateMapper, Operate> implements OperateService {
    @Autowired
    OperateMapper operateMapper;
    @Override
    public List<MessageReturn> getRecentMsg(int partner) {
        return operateMapper.getRecentMsg(partner);
    }
}
