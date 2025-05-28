package com.busymango.busymangoBackend.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.project.model.dto.SearchRecordDTO;
import com.busymango.busymangoBackend.project.model.entity.SearchRecord;
import com.busymango.busymangoBackend.project.service.ProjectService;
import com.busymango.busymangoBackend.project.service.SearchRecordService;
import com.busymango.busymangoBackend.project.mapper.SearchRecordMapper;
import com.busymango.busymangoBackend.user.model.context.UserContext;
import com.busymango.busymangoBackend.user.model.dto.UserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 95788
 * @description 针对表【bcmcreate_search_record(搜索记录表)】的数据库操作Service实现
 * @createDate 2025-02-10 14:30:36
 */
@Service
public class SearchRecordServiceImpl extends ServiceImpl<SearchRecordMapper, SearchRecord>
        implements SearchRecordService {
    @Resource
    private SearchRecordMapper searchRecordMapper;

    @Override
    public void saveSearchRecord(String keyword) {
        UserDTO userDTO = UserContext.getUserDTO();
        SearchRecordDTO searchRecordDTO = new SearchRecordDTO();
        searchRecordDTO.setKeyword(keyword);
        searchRecordDTO.setUserId(userDTO.getUserId());
        searchRecordMapper.saveSearchRecord(searchRecordDTO);
    }
}




