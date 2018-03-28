package com.deep.api.resource;

import com.deep.api.response.Response;
import com.deep.api.response.Responses;
import com.deep.domain.model.DiagnosisPlanExample;
import com.deep.domain.model.DiagnosisPlanWithBLOBs;
import com.deep.domain.model.OtherTime;
import com.deep.domain.service.DiagnosisPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * author: Created  By  Caojiawei
 * date: 2018/2/18  11:46
 */
@Controller
public class DiagnosisResource {
    @Resource
    private DiagnosisPlanService diagnosisPlanService;

    @ResponseBody
    @RequestMapping(value = "/diagnosisPlan",method = RequestMethod.GET)
    public String helloDiagnosis() {
        return "Hello DiagnosisPlan!";
    }

//    按主键删除的接口：/diagnosisInsert
//    按主键删除的方法名：addPlan()
//    接收参数：整个表单信息（所有参数必填）
//    参数类型为：Long factoryNum;Date diagnosisT;String building;String etB;String operator;String remark;String diagnosisC;String diagnosisM;String drugQ;
    @RequestMapping(value = "/diagnosisInsert",method = RequestMethod.GET)
    public String addPlan(){
        return "DiagnosisInsert";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisInsert/show",method = RequestMethod.POST)
    public Response addPlan(@RequestBody DiagnosisPlanWithBLOBs insert) throws ParseException {
        Date diagnosisT = new Date();
        Byte zero = 0;
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS");
        if (insert.getDiagnosisT().toString() != ""){
            diagnosisT =  formatter.parse(insert.getDiagnosisT().toString());
        }
        insert.setGmtCreate(new Date());
        insert.setFactoryNum(insert.getFactoryNum());
        insert.setBuilding(insert.getBuilding());
        insert.setDiagnosisT(diagnosisT);
        insert.setEtB(insert.getEtB());
        insert.setOperator(insert.getOperator());
        insert.setRemark(insert.getRemark());
        insert.setIsPass(zero);
        insert.setIsPass1(zero);
        insert.setDiagnosisC(insert.getDiagnosisC());
        insert.setDiagnosisM(insert.getDiagnosisM());
        insert.setDrugQ(insert.getDrugQ());
        diagnosisPlanService.addPlan(insert);

        DiagnosisPlanExample diagnosisPlanExample = new DiagnosisPlanExample();
        DiagnosisPlanExample.Criteria criteria = diagnosisPlanExample.createCriteria();
        criteria.andFactoryNumEqualTo(insert.getFactoryNum());
        criteria.andBuildingEqualTo(insert.getBuilding());
        criteria.andEtBEqualTo(insert.getEtB());
        insert.setDiagnosisT(diagnosisT);
        criteria.andOperatorEqualTo(insert.getOperator());
        criteria.andRemarkEqualTo(insert.getRemark());
        List<DiagnosisPlanWithBLOBs> select = diagnosisPlanService.findPlanSelective(diagnosisPlanExample);

        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",select);
        response.setData(data);
        return response;
    }

//    按主键删除的接口：/diagnosisDeleteById
//    按主键删除的方法名：dropPlan()
//    接收参数：整型id，根据主键号删除
    @RequestMapping(value = "/diagnosisDeleteById",method = RequestMethod.GET)
    public String dropPlan(){
        return "DiagnosisDeleteById";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisDeleteById/show",method = RequestMethod.DELETE)
    public Response dropPlan(@RequestBody DiagnosisPlanWithBLOBs diagnosisPlanWithBLOBs){
        DiagnosisPlanWithBLOBs delete = new DiagnosisPlanWithBLOBs();
        diagnosisPlanService.dropPlan(diagnosisPlanWithBLOBs.getId());
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",delete);
        response.setData(data);
        return response;
    }

//    专家使用按主键修改的接口：/diagnosisUpdateByProfessor
//    专家使用按主键修改的方法名：changePlanByProfessor()
//    专家使用接收参数：整个表单类型
    @RequestMapping(value = "/diagnosisUpdateByProfessor",method = RequestMethod.GET)
    public String changePlanByProfessor(){
        return "DiagnosisUpdateByProfessor";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisUpdateByProfessor/show",method = RequestMethod.PUT)
    public Response changePlanByProfessor(@RequestBody DiagnosisPlanWithBLOBs update) throws ParseException {
        Date diagnosisT = new Date();
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS");
        if (update.getDiagnosisT().toString() != ""){
            diagnosisT =  formatter.parse(update.getDiagnosisT().toString());
        }
        update.setId(update.getId());
        update.setGmtModified(new Date());
        update.setFactoryNum(update.getFactoryNum());
        update.setDiagnosisT(diagnosisT);
        update.setBuilding(update.getBuilding());
        update.setEtB(update.getEtB());
        update.setProfessor(update.getProfessor());
        update.setRemark(update.getRemark());
        update.setIsPass(update.getIsPass());
        update.setUpassReason(update.getUpassReason());
        update.setDiagnosisC(update.getDiagnosisC());
        update.setDiagnosisM(update.getDiagnosisM());
        update.setDrugQ(update.getDrugQ());

        diagnosisPlanService.changePlanByProfessor(update);
        DiagnosisPlanWithBLOBs selectById = diagnosisPlanService.findPlanById(update.getId());
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",selectById);
        response.setData(data);
        return response;
    }

//    监督者使用按主键修改的接口：/diagnosisUpdateBySupervisor
//    监督者使用按主键修改的方法名：changePlanBySupervisor()
//    监督者使用接收参数：整个表单信息（整型id必填，各参数选填）
    @RequestMapping(value = "/diagnosisUpdateBySupervisor",method = RequestMethod.GET)
    public String changePlanBySupervisor(){
        return "DiagnosisUpdateBySupervisor";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisUpdateBySupervisor/show",method = RequestMethod.PUT)
    public Response changePlanBySupervisor(@Valid DiagnosisPlanWithBLOBs update){
        update.setId(update.getId());
        update.setGmtSupervised(new Date());
        update.setSupervisor(update.getSupervisor());
        update.setIsPass1(update.getIsPass1());

        diagnosisPlanService.changePlanBySupervisor(update);
        DiagnosisPlanWithBLOBs selectById = diagnosisPlanService.findPlanById(update.getId());
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",selectById);
        response.setData(data);
        return response;
    }

//    按主键查询的接口：/diagnosisSelectById
//    按主键查询的方法名：findPlanById()
//    接收参数：整型的主键号（保留接口查询，前端不调用此接口）
    @RequestMapping(value = "/diagnosisSelectById",method = RequestMethod.GET)
    public String findPlanById(){
        return "DiagnosisSelectById";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisSelectById/show",method = RequestMethod.GET)
    public Response findPlanById(@Valid DiagnosisPlanWithBLOBs diagnosisPlanWithBLOBs){
        //查询语句的写法：一定要在声明对象时把值直接赋进去
        DiagnosisPlanWithBLOBs selectById = diagnosisPlanService.findPlanById(diagnosisPlanWithBLOBs.getId());
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",selectById);
        response.setData(data);
        return response;
    }

//    按条件查询接口：/diagnosisSelective
//    按条件查询方法名：findPlanSelective()
//    接收的参数：前端的各参数，以及两个("s_diagnosisT1")("s_diagnosisT2")时间字符串（所有参数可以选填）
    @RequestMapping(value = "/diagnosisSelective",method = RequestMethod.GET)
    public String findPlanSelective(){
        return "DiagnosisSelective";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisSelective/show",method = RequestMethod.GET)
    public Response findPlanSelective(@Valid DiagnosisPlanWithBLOBs diagnosisPlanWithBLOBs,
                                      @Valid OtherTime otherTime) throws ParseException {
        Date diagnosisT1 = null;
        Date diagnosisT2 = null;
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS");
        DiagnosisPlanExample diagnosisPlanExample = new DiagnosisPlanExample();
        DiagnosisPlanExample.Criteria criteria = diagnosisPlanExample.createCriteria();
        if (otherTime.getS_diagnosisT1() != null && otherTime.getS_diagnosisT1() != "" && otherTime.getS_diagnosisT2() != null && otherTime.getS_diagnosisT2() != ""){
            diagnosisT1 =  formatter.parse(otherTime.getS_diagnosisT1());
            diagnosisT2 =  formatter.parse(otherTime.getS_diagnosisT2());
        }
        if(diagnosisT1 != null && diagnosisT2 != null){
            criteria.andDiagnosisTBetween(diagnosisT1,diagnosisT2);
        }
        if(diagnosisPlanWithBLOBs.getId() != null && diagnosisPlanWithBLOBs.getId().toString() !=""){
            criteria.andIdEqualTo(diagnosisPlanWithBLOBs.getId());
        }
        if(diagnosisPlanWithBLOBs.getFactoryNum() != null && diagnosisPlanWithBLOBs.getFactoryNum().toString() !=""){
            criteria.andFactoryNumEqualTo(diagnosisPlanWithBLOBs.getFactoryNum());
        }
        if(diagnosisPlanWithBLOBs.getBuilding() != null && diagnosisPlanWithBLOBs.getBuilding() !=""){
            criteria.andBuildingEqualTo(diagnosisPlanWithBLOBs.getBuilding());
        }
        if(diagnosisPlanWithBLOBs.getEtB() != null && diagnosisPlanWithBLOBs.getEtB() !=""){
            criteria.andEtBEqualTo(diagnosisPlanWithBLOBs.getEtB());
        }
        if(diagnosisPlanWithBLOBs.getOperator() != null && diagnosisPlanWithBLOBs.getOperator() !=""){
            criteria.andOperatorEqualTo(diagnosisPlanWithBLOBs.getOperator());
        }
        if(diagnosisPlanWithBLOBs.getProfessor() != null && diagnosisPlanWithBLOBs.getProfessor() !=""){
            criteria.andProfessorEqualTo(diagnosisPlanWithBLOBs.getProfessor());
        }
        if(diagnosisPlanWithBLOBs.getSupervisor() != null && diagnosisPlanWithBLOBs.getSupervisor() !=""){
            criteria.andSupervisorEqualTo(diagnosisPlanWithBLOBs.getSupervisor());
        }
        if(diagnosisPlanWithBLOBs.getRemark() != null && diagnosisPlanWithBLOBs.getRemark() !=""){
            criteria.andRemarkLike(diagnosisPlanWithBLOBs.getRemark());
        }
        if(diagnosisPlanWithBLOBs.getIsPass() != null && diagnosisPlanWithBLOBs.getIsPass().toString() !=""){
            criteria.andIsPassEqualTo(diagnosisPlanWithBLOBs.getIsPass());
        }
        if(diagnosisPlanWithBLOBs.getUpassReason() != null && diagnosisPlanWithBLOBs.getUpassReason() !=""){
            criteria.andUpassReasonLike(diagnosisPlanWithBLOBs.getUpassReason());
        }
        if(diagnosisPlanWithBLOBs.getIsPass1() != null && diagnosisPlanWithBLOBs.getIsPass1().toString() !=""){
            criteria.andIsPassEqualTo(diagnosisPlanWithBLOBs.getIsPass1());
        }
        List<DiagnosisPlanWithBLOBs> select = diagnosisPlanService.findPlanSelective(diagnosisPlanExample);
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",select);
        response.setData(data);
        return response;
    }

//    供技术审核查询信息:/diagnosisSelectByProfessor
//    供技术审核查询方法名：findPlanSelectByProfessor()
//    接收的参数：前端的各参数，（所有参数可以选填）
    @RequestMapping(value = "/diagnosisSelectByProfessor",method = RequestMethod.GET)
    public String findPlanByProfessor(){
        return "DiagnosisSelectByProfessor";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisSelectByProfessor/show",method = RequestMethod.GET)
    public Response findPlanByProfessor(@Valid DiagnosisPlanWithBLOBs diagnosisPlanWithBLOBs){
        DiagnosisPlanExample diagnosisPlanExample = new DiagnosisPlanExample();
        DiagnosisPlanExample.Criteria criteria = diagnosisPlanExample.createCriteria();
        Byte notPassed = 0;
        if(diagnosisPlanWithBLOBs.getId() != null && diagnosisPlanWithBLOBs.getId().toString() !=""){
            criteria.andIdEqualTo(diagnosisPlanWithBLOBs.getId());
        }
        if(diagnosisPlanWithBLOBs.getProfessor() != null && diagnosisPlanWithBLOBs.getProfessor() !=""){
            criteria.andProfessorEqualTo(diagnosisPlanWithBLOBs.getProfessor());
        }
        if(diagnosisPlanWithBLOBs.getUpassReason() != null && diagnosisPlanWithBLOBs.getUpassReason() !=""){
            criteria.andUpassReasonLike(diagnosisPlanWithBLOBs.getUpassReason());
        }
        criteria.andIsPassEqualTo(notPassed);
        List<DiagnosisPlanWithBLOBs> select = diagnosisPlanService.findPlanSelective(diagnosisPlanExample);
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",select);
        response.setData(data);
        return response;
    }

//    供监督者查询信息:/diagnosisSelectBySupervisor
//    供监督者查询方法名：findPlanSelectBySupervisor()
//    接收的参数：前端的各参数，（所有参数可以选填）
    @RequestMapping(value = "/diagnosisSelectBySupervisor",method = RequestMethod.GET)
    public String findPlanSelectBySupervisor(){
        return "DiagnosisSelectBySupervisor";
    }
    @ResponseBody
    @RequestMapping(value = "/diagnosisSelectBySupervisor/show",method = RequestMethod.GET)
    public Response findPlanSelectBySupervisor(@Valid DiagnosisPlanWithBLOBs diagnosisPlanWithBLOBs){
        DiagnosisPlanExample diagnosisPlanExample = new DiagnosisPlanExample();
        DiagnosisPlanExample.Criteria criteria = diagnosisPlanExample.createCriteria();
        Byte notPassed1 = 0;
        if(diagnosisPlanWithBLOBs.getId() != null && diagnosisPlanWithBLOBs.getId().toString() !=""){
            criteria.andIdEqualTo(diagnosisPlanWithBLOBs.getId());
        }
        if(diagnosisPlanWithBLOBs.getSupervisor() != null && diagnosisPlanWithBLOBs.getSupervisor() !=""){
            criteria.andSupervisorEqualTo(diagnosisPlanWithBLOBs.getSupervisor());
        }
        criteria.andIsPassEqualTo(notPassed1);
        List<DiagnosisPlanWithBLOBs> select = diagnosisPlanService.findPlanSelective(diagnosisPlanExample);
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("diagnosis_plan",select);
        response.setData(data);
        return response;
    }
}
