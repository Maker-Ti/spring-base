package com.maker.demo.mapper;

import com.maker.demo.entity.Library;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maker.demo.entity.MessageReturn;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Maker
 * @since 2021-04-22
 */
public interface LibraryMapper extends BaseMapper<Library> {
    List<Library> getLibraryList(int creator);
    List<Library> getJoinedLibrary(int partner);
    List<MessageReturn> searchInfo(String info);
    List<MessageReturn> getRecomLib(String query);
}
