package com.deep.domain.service;


import com.deep.api.request.DiagnosisRequest;
import com.deep.domain.model.DiagnosisPlanModel;
import com.deep.infra.persistence.sql.mapper.DiagnosisPlanMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * author: Created  By  Caojiawei
 * date: 2018/2/18  11:19
 */
@Service
public class DiagnosisPlanService {
    @Resource
    private DiagnosisPlanMapper diagnosisPlanMapper;


    public int addPlan(DiagnosisPlanModel diagnosisPlanModel) {
        return this.diagnosisPlanMapper.insert(diagnosisPlanModel);
    }

    public int dropPlan(Integer id) {
        return this.diagnosisPlanMapper.deleteByPrimaryKey(id);
    }

    public int checkDiagnosisPlanModelById(int id, short isPassCheck, int professorId) {
        return this.diagnosisPlanMapper.checkDiagnosisPlanModelById(id, isPassCheck, professorId);
    }

    public int supCheckDiagnosisPlanModelById(int id, short ispassSup, String upassReason) {
        return this.diagnosisPlanMapper.supCheckDiagnosisPlanModelById(id, ispassSup, upassReason);
    }

    public int updateDiagnosisPlanModel(DiagnosisPlanModel diagnosisPlanModel) {
        return this.diagnosisPlanMapper.updateDiagnosisPlanModel(diagnosisPlanModel);
    }

    public DiagnosisPlanModel findPlanById(Integer id) {
        return this.diagnosisPlanMapper.selectByPrimaryKey(id);
    }

    public List<DiagnosisPlanModel> selectDiagnosisPlanModelByDiagnosisRequest(DiagnosisRequest diagnosisRequest, RowBounds rowBounds) {
        return this.diagnosisPlanMapper.selectDiagnosisPlanModelByDiagnosisRequest(diagnosisRequest,rowBounds);
    }

//    public List<DiagnosisPlanModel> professorFindPlan(long factoryNum,
//                                                      int ispassSup,
//                                                      RowBounds rowBounds) {
//
//    }

}
