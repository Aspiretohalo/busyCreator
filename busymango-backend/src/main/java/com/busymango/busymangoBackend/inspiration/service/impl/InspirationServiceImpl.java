package com.busymango.busymangoBackend.inspiration.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.busymango.busymangoBackend.inspiration.model.entity.Inspiration;
import com.busymango.busymangoBackend.inspiration.model.enums.ContentTypeEnum;
import com.busymango.busymangoBackend.inspiration.model.vo.InspirationVO;
import com.busymango.busymangoBackend.inspiration.service.InspirationService;
import com.busymango.busymangoBackend.inspiration.mapper.InspirationMapper;
import com.busymango.busymangoBackend.task.mapper.TaskMapper;
import com.busymango.busymangoBackend.task.model.vo.TaskVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Admin
 * @description 针对表【bcmcreate_inspiration(灵感表)】的数据库操作Service实现
 * @createDate 2025-03-23 13:57:54
 */
@Service
public class InspirationServiceImpl extends ServiceImpl<InspirationMapper, Inspiration>
        implements InspirationService {
    @Resource
    private InspirationMapper inspirationMapper;

    @Override
    public List<InspirationVO> listInspiration(String inspiration) {
        if (inspiration == null || inspiration.isEmpty()) {
            // 如果输入为空，直接返回空列表或抛出异常
            return Collections.emptyList();
        }

        // 判断是否包含“广告”或“小说”
        if (inspiration.contains("广告")) {
            return inspirationMapper.listInspirationByType(ContentTypeEnum.AD.getType());
        } else if (inspiration.contains("小说")) {
            return inspirationMapper.listInspirationByType(ContentTypeEnum.NOVEL.getType());
        } else if (inspiration.contains("新闻")) {
            return inspirationMapper.listInspirationByType(ContentTypeEnum.NEWS.getType());
        } else if (inspiration.contains("评论")) {
            return inspirationMapper.listInspirationByType(ContentTypeEnum.COMMENT.getType());
        } else {
            // 如果不包含“广告”或“小说”，可以返回空列表或抛出异常
            return inspirationMapper.listInspiration();
        }
    }
}




