package com.deep.infra.persistence.sql.mapper;

import com.deep.domain.model.AgentModel;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AgentMapper {

    @Select("select count(*) from agent_factory where agent_rank != 0")
    Long queryCount();

    /**
     * 列出代理列表
     * @return
     */
    @Select("select * from agent_factory where agent_rank != 0 order by agent_rank asc limit #{start}, #{size}")
    @Results ({
            @Result(property = "id", column = "id"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "agentName", column = "agent_name"),
            @Result(property = "agentArea", column = "agent_area"),
            @Result(property = "agentFather", column = "agent_father"),
            @Result(property = "agentRank", column = "agent_rank"),
            @Result(property = "responsibleId", column = "responsible_id"),
            @Result(property = "responsibleName", column = "responsible_name")
    })
    List<AgentModel> queryAllAgent(@Param("start") Long start, @Param("size") Byte size);

    /**
     * 获取所有的子代理
     * @param id
     * @return
     */
    @Select("select * from agent_factory where agent_father = #{id} and agent_rank != 0 order by agent_rank asc")
    @Results ({
            @Result(property = "id", column = "id"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "agentName", column = "agent_name"),
            @Result(property = "agentArea", column = "agent_area"),
            @Result(property = "agentFather", column = "agent_father"),
            @Result(property = "agentRank", column = "agent_rank"),
            @Result(property = "responsibleId", column = "responsible_id"),
            @Result(property = "responsibleName", column = "responsible_name")
    })
    List<AgentModel> getSons(int id);

    /**
     * 获取上级代理
     * @param id
     * @return
     */
    @Select("select * from agent_factory where id = #{id} order by agent_rank asc")
    @Results ({
            @Result(property = "id", column = "id"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "agentName", column = "agent_name"),
            @Result(property = "agentArea", column = "agent_area"),
            @Result(property = "agentFather", column = "agent_father"),
            @Result(property = "agentRank", column = "agent_rank"),
            @Result(property = "responsibleId", column = "responsible_id"),
            @Result(property = "responsibleName", column = "responsible_name")
    })
    AgentModel getFather(int id);

    /**
     * 根据ID获取单个代理
     * @param id
     * @return
     */
    @Select("select * from agent_factory where id = #{id}")
    @Results ({
            @Result(property = "id", column = "id"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "agentName", column = "agent_name"),
            @Result(property = "agentArea", column = "agent_area"),
            @Result(property = "agentFather", column = "agent_father"),
            @Result(property = "agentRank", column = "agent_rank"),
            @Result(property = "responsibleId", column = "responsible_id"),
            @Result(property = "responsibleName", column = "responsible_name")
    })
    AgentModel queryAgentByID(Long id);

    /**
     * 插入一个代理
     * @param agentModel
     * @return
     */
    @Insert("insert into agent_factory(" +
            "gmt_create, " +
            "gmt_modified, " +
            "agent_rank, " +
            "agent_area, " +
            "agent_name, " +
            "agent_father," +
            "responsible_id," +
            "responsible_name" +
            ") values(" +
            "#{gmtCreate}, " +
            "#{gmtModified}, " +
            "#{agentRank}," +
            "#{agentArea}, " +
            "#{agentName}, " +
            "#{agentFather}, " +
            "#{responsibleId}," +
            "#{responsibleName}" +
            ")")
    Long insertAgent(AgentModel agentModel);

    /**
     * 修改一个代理
     * @param agentModel
     * @return
     */
    @Update("update agent_factory set " +
            "gmt_create = #{gmtCreate}, " +
            "gmt_modified = #{gmtModified}, " +
            "agent_rank = #{agentRank}, " +
            "agent_area = #{agentArea}, " +
            "agent_name = #{agentName}, " +
            "agent_father = #{agentFather}," +
            "responsible_id = #{responsibleId}," +
            "responsible_name = #{responsibleName}" +
            " where id = #{id}")
    Long updateAgent(AgentModel agentModel);

    /**
     * 删除一个代理
     * @param permitID
     * @return
     */
    @Delete("delete from agent_factory where id = #{id}")
    Long deleteAgent(Long permitID);
}
