package com.experian.daas.litigation.dao;

import com.experian.dto.entity.litigation.LitigationTransmissionLog;
import java.util.List;

public interface LitigationTransmissionLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(LitigationTransmissionLog record);

    LitigationTransmissionLog selectByPrimaryKey(Integer id);

    List<LitigationTransmissionLog> selectAll();

    int updateByPrimaryKey(LitigationTransmissionLog record);
    
    LitigationTransmissionLog selectLastestLog();
}