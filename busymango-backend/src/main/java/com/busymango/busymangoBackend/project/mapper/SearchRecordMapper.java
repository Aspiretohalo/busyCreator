package com.busymango.busymangoBackend.project.mapper;

import com.busymango.busymangoBackend.project.model.dto.SearchRecordDTO;
import com.busymango.busymangoBackend.project.model.entity.SearchRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 95788
* @description 针对表【bcmcreate_search_record(搜索记录表)】的数据库操作Mapper
* @createDate 2025-02-10 14:30:36
* @Entity com.busymango.busymangoBackend.project.model.entity.SearchRecord
*/
public interface SearchRecordMapper extends BaseMapper<SearchRecord> {

    void saveSearchRecord(SearchRecordDTO searchRecordDTO);
}




