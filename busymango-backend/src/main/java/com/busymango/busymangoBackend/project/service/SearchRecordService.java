package com.busymango.busymangoBackend.project.service;

import com.busymango.busymangoBackend.project.model.entity.SearchRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 95788
* @description 针对表【bcmcreate_search_record(搜索记录表)】的数据库操作Service
* @createDate 2025-02-10 14:30:36
*/
public interface SearchRecordService extends IService<SearchRecord> {

    void saveSearchRecord(String keyword);
}
