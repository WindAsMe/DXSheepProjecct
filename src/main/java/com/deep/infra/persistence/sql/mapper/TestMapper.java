package com.deep.infra.persistence.sql.mapper;

import com.deep.domain.model.TestModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * Created by huangwenhai on 2018/1/31.
 */

@Mapper
public interface TestMapper {

  //@Select("SELECT * FROM P")
  TestModel getTestModelById(@Param("id") String id);
}
