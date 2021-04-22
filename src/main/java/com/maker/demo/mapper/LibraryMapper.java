package com.maker.demo.mapper;

import com.maker.demo.entity.Library;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Maker
 * @since 2021-04-18
 */
public interface LibraryMapper extends BaseMapper<Library> {
    List<Library> getLibraryList(int creator);
}
