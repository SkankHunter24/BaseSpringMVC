package com.dao;

import java.util.List;

import com.model.vo.Attachment;

public interface AttachmentMapper {

    int deleteByPrimaryKey(String attachmentId);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(String attachmentId);

    List<Attachment> selectByRelationId_singleOpreation(String relationId,String uploaderId,String uploaderControlId);
    
    List<Attachment> selectByRelationId_controlId_uploaderId(String relationId,String uploaderControlId,String uploaderId);
    
    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);
}