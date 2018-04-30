package com.deep.infra.persistence.sql.mapper;


import com.deep.api.request.ImmuneRequest;
import com.deep.api.request.ModuleFindRequest;
import com.deep.api.request.ModuleUpdateRequest;
import com.deep.domain.model.ImmunePlanModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ImmunePlanMapper {
    int setImmunePlanModel(@Param("immunePlanModel") ImmunePlanModel immunePlanModel);


    List<ImmunePlanModel> getImmunePlanModel(@Param("immunePlanModel") ImmuneRequest immunePlanModel,
                                             RowBounds bounds);

    ImmunePlanModel getImmunePlanModelById(@Param("id") Long id);

    List<ImmunePlanModel> getImmunePlanModelByFactoryNum(@Param("factoryNum")BigInteger factoryNum , RowBounds bounds);

    List<ImmunePlanModel> getImmunePlanModelByIsPassCheck(@Param("request") ModuleFindRequest request , RowBounds bounds);

    List<ImmunePlanModel> getImmunePlanModelByIsPassSup(@Param("request") ModuleFindRequest request , RowBounds bounds);

    int updateImmunePlanModelByProfessor(@Param("request") ModuleUpdateRequest request);

    int updateImmunePlanModelBySupervisor(@Param("request") ModuleUpdateRequest request);

    int updateImmunePlanModelByOperator(@Param("immunePlanModel") ImmunePlanModel immunePlanModel);

    int deleteImmunePlanModelById(@Param("id") Long id);


}
