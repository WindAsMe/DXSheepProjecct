package com.deep.domain.service;

import com.deep.infra.persistence.sql.mapper.ExpressionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpressionService {
    @Autowired
    ExpressionMapper expressionMapper;

    public int expression_record(Long expert_id, String expression){
        return expressionMapper.expression_record(expert_id, expression);
    }
}
