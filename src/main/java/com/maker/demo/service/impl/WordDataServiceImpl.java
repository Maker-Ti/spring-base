package com.maker.demo.service.impl;

import com.maker.demo.entity.WordData;
import com.maker.demo.mapper.WordDataMapper;
import com.maker.demo.service.WordDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Maker
 * @since 2021-04-22
 */
@Service
public class WordDataServiceImpl extends ServiceImpl<WordDataMapper, WordData> implements WordDataService {

}
