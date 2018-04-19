package com.deep.infra.persistence.sql.mapper;

import com.deep.domain.model.TypeBriefModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * create by zhongrui on 18-4-17.
 */
@Mapper
public interface TypeBriefMapper {

    void setTypeBrief(@Param("typeBriefModel") TypeBriefModel typeBriefModel);
    List<String> getAllType();
    List<TypeBriefModel> getAll();
    TypeBriefModel getTypeBrief(@Param("type") String type);

    int updateTypeBrief(@Param("typeBriefModel") TypeBriefModel typeBriefModel);

}
