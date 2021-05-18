package com.maker.demo.service.impl;

import com.maker.demo.entity.Library;
import com.maker.demo.entity.MessageReturn;
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
 * @since 2021-04-22
 */
@Service
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper, Library> implements LibraryService {
    @Autowired
    LibraryMapper libraryMapper;
    @Override
    public List<Library> getLibraryList(int creator) {
        return libraryMapper.getLibraryList(creator);
    }

    @Override
    public List<Library> getJoinedLibrary(int partner) {
        return libraryMapper.getJoinedLibrary(partner);
    }

    @Override
    public List<MessageReturn> searchInfo(String info) {
        return libraryMapper.searchInfo(info);
    }

    @Override
    public List<MessageReturn> getRecomLib(String query) {
        return libraryMapper.getRecomLib(query);
    }
}
