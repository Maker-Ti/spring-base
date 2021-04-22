package com.maker.demo.service;

import com.maker.demo.entity.Library;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Maker
 * @since 2021-04-18
 */
public interface LibraryService extends IService<Library> {
    List<Library> getLibraryList(int creator);
}
