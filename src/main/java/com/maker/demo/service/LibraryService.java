package com.maker.demo.service;

import com.maker.demo.entity.Library;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maker.demo.entity.MessageReturn;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Maker
 * @since 2021-04-22
 */
public interface LibraryService extends IService<Library> {
    List<Library> getLibraryList(int creator);
    List<Library> getJoinedLibrary(int partner);
    List<MessageReturn> searchInfo(String info);
    List<MessageReturn> getRecomLib(String query);
}
