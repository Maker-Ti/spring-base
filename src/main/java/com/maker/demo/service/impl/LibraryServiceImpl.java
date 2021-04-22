package com.maker.demo.service.impl;

import com.maker.demo.entity.Library;
import com.maker.demo.mapper.LibraryMapper;
import com.maker.demo.service.LibraryService;
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
 * @since 2021-04-18
 */
@Service
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper, Library> implements LibraryService {
    @Autowired
    LibraryMapper libraryMapper;

    @Override
    public List<Library> getLibraryList(int creator) {
        return libraryMapper.getLibraryList(creator);
    }
}
