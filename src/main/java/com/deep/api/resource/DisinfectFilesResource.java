package com.deep.api.resource;

import com.deep.api.Utils.AgentUtil;
import com.deep.api.Utils.TokenAnalysis;
import com.deep.api.authorization.tools.Constants;
import com.deep.api.request.DisinfectRequest;
import com.deep.api.response.Response;
import com.deep.api.response.Responses;
import com.deep.domain.model.DisinfectFilesModel;
import com.deep.domain.service.DisinfectFilesService;
import com.deep.domain.service.FactoryService;
import com.deep.domain.service.UserService;
import com.deep.domain.util.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

@RestController
@RequestMapping(value = "/df")
public class DisinfectFilesResource {
    private final Logger logger = LoggerFactory.getLogger(DisinfectFilesResource.class);
    private final String pathPre = "../EartagDocument/";
    @Resource
    private DisinfectFilesService disinfectFilesService;

    //用于查询专家/监督员电话并抉择发送短信
    @Resource
    private UserService userService;

    //用于查询羊厂代理id
    @Resource
    private FactoryService factoryService;

    /**
     * 返回插入结果
     * 成功：success
     * 失败：返回对应失败错误
     *
     * 同时 若累计未审核超过50条（自定义）
     * 3天内未通知对应专家/审核员
     * 则通知 不然返回已发送
     * METHOD:POST
     * @param disinfectFilesModel 消毒类
     * @return 插入结果
     */
    @PostMapping(value = "")
    public Response saveShow(@Valid DisinfectFilesModel disinfectFilesModel,
                             BindingResult bindingResult,
                             @RequestParam(value = "eartagFile") MultipartFile disinfectEartagFile,
                             HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            Response response = Responses.errorResponse("param is error");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("param",bindingResult.getAllErrors());
            response.setData(map);
            return response;
        }

        logger.info("invoke Post /df {}", disinfectFilesModel);
        if( disinfectEartagFile.isEmpty() ) {
            return Responses.errorResponse("Lack Item");
        } else {
            try {
                //System.out.println("running");
                String fileName = disinfectEartagFile.getOriginalFilename();
                //目的路径
                String filePath = pathPre + disinfectFilesModel.getFactoryNum().toString() + "/disinfectEartag/";
                fileName = UploadUtil.uploadFile(disinfectEartagFile.getBytes(),filePath,fileName);
                //System.out.println("save before");

                //数据插入数据库
                //System.out.println("mysql执行前");

                disinfectFilesModel.setDisinfectEartag(fileName);

                disinfectFilesService.setDisinfectFilesModel(disinfectFilesModel);

                //System.out.println("pppppppppppppppppppppppppppp  "+ disinfectFilesModel.getId());
                //数据插入redis
                //professor字段为 代理ID + _professor
                //supervisor字段为 工厂号 + _supervisor
                short agentID = this.factoryService.getAgentIDByFactoryNumber(disinfectFilesModel.getFactoryNum().toString());
                String professorKey = agentID + "_professor";
                String supervisorKey = disinfectFilesModel.getFactoryNum().toString() + "_supervisor";

                String testSendProfessor = agentID + "_professor_AlreadySend";
                String testSendSupervisor = disinfectFilesModel.getFactoryNum().toString() + "_supervisor_AlreadySend";

                JedisUtil.redisSaveProfessorSupervisorWorks(professorKey);
                JedisUtil.redisSaveProfessorSupervisorWorks(supervisorKey);



                System.out.println("插入后,审核前");
                System.out.println("pk+"+professorKey+" "+"pv:"+JedisUtil.getCertainKeyValue(professorKey));
                System.out.println("sk+"+supervisorKey+" "+"sv:"+JedisUtil.getCertainKeyValue(supervisorKey));
                System.out.println("tpk+"+testSendProfessor+" "+"tpv:"+JedisUtil.getCertainKeyValue(testSendProfessor));
                System.out.println("tsk+"+testSendSupervisor+" "+"tsv:"+JedisUtil.getCertainKeyValue(testSendSupervisor));

                //发送短信后 testSendProfessor存放在redis中 过期时间为ExpireTime
                if( !("1".equals(JedisUtil.getCertainKeyValue(testSendProfessor)))) {
                    //System.out.println("testSendProfessorValue:"+jedisUtil.getCertainKeyValue(testSendProfessor));
                    if( JedisUtil.redisJudgeTime(professorKey) ) {

                        System.out.println("in redis:");

                        List<String> phone = userService.getProfessorTelephoneByFactoryNum(disinfectFilesModel.getFactoryNum());

                        //需完成:userModels.getTelephone()赋值给String
                        // 获得StringBuffer手机号
                        StringBuffer phoneList = new StringBuffer("");

                        for (String aPhone : phone) {
                            phoneList = phoneList.append(aPhone).append(",");
                        }

                        if (phoneList.length() != 0) {
                            if (JedisUtil.redisSendMessage(phoneList.toString(), JedisUtil.getCertainKeyValue("Message"))) {
                                JedisUtil.setCertainKeyValueWithExpireTime(testSendProfessor, "1", Integer.parseInt(JedisUtil.getCertainKeyValue("ExpireTime")) * 24 * 60 * 60);
                            }
                        }


                    }
                }

                if( !("1".equals(JedisUtil.getCertainKeyValue(testSendSupervisor)))) {
                    if(JedisUtil.redisJudgeTime(supervisorKey)) {
                        List<String> phone = userService.getSuperiorTelephoneByFactoryNum(disinfectFilesModel.getFactoryNum());

                        StringBuffer phoneList = new StringBuffer("");

                        for (String aPhone : phone) {
                            phoneList = phoneList.append(aPhone).append(",");
                        }

                        if (phoneList.length() != 0) {

                            if( JedisUtil.redisSendMessage(phoneList.toString(), JedisUtil.getCertainKeyValue("Message"))) {
                                JedisUtil.setCertainKeyValueWithExpireTime(testSendSupervisor,"1",Integer.parseInt(JedisUtil.getCertainKeyValue("ExpireTime"))*24*60*60);
                            }
                        }
                    }
                }
                return JudgeUtil.JudgeSuccess("id",disinfectFilesModel.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Responses.errorResponse("Exception");

    }

    /**
     * 返回查询结果
     * 以json格式返回前端
     * 分页查询
     * METHOD:POST
     * @param disinfectRequest 消毒请求类
     * @return 查询结果/查询结果条数
     */
    @GetMapping(value = "/{id}")
    public Response findShow(@PathVariable(value = "id")long id,
                             DisinfectRequest disinfectRequest,
                             HttpServletRequest httpServletRequest) {
        logger.info("invoke get /{} {}",id,disinfectRequest);

        Map<Long, List<Long>> factoryMap = null;
        Byte role = Byte.parseByte(TokenAnalysis.getFlag(httpServletRequest.getHeader(Constants.AUTHORIZATION)));
        if (role == 0) {
          disinfectRequest.setFactoryNum(id);
        } else if (role == 1) {
          factoryMap = AgentUtil.getAllSubordinateFactory(String.valueOf(id));
          System.out.println(factoryMap);
          List<Long> factoryList = new ArrayList<>();
          factoryList.addAll(factoryMap.get(new Long(-1)));
          factoryList.addAll(factoryMap.get(new Long(0)));
          disinfectRequest.setFactoryList(factoryList);
        } else {
          return Responses.errorResponse("你没有权限");
        }

        List<DisinfectFilesModel> disinfectFilesModels = disinfectFilesService.getDisinfectFilesModel(disinfectRequest,
            new RowBounds(disinfectRequest.getPage() * disinfectRequest.getSize(),disinfectRequest.getSize()));

        if (role == 1) {
          Map<String,Object> data = new HashMap<>();
          List<DisinfectFilesModel> factorylist = new ArrayList<>();
          List<DisinfectFilesModel> direct = new ArrayList<>();
          List<DisinfectFilesModel> others = new ArrayList<>();
          List<Long> directId = factoryMap.get(new Long(-1));
          for (DisinfectFilesModel disinfectFilesModel : disinfectFilesModels) {
            if (directId.contains(disinfectFilesModel.getFactoryNum())) {
              direct.add(disinfectFilesModel);
            } else {
              others.add(disinfectFilesModel);
            }
          }
          factorylist.addAll(direct);
          factorylist.addAll(others);
          data.put("List", factorylist);
          data.put("size", factorylist.size());
          data.put("directSize",direct.size());
          Response response = Responses.successResponse();
          response.setData(data);
          return response;
        } else {
          return JudgeUtil.JudgeFind(disinfectFilesModels,disinfectFilesModels.size());
        }
    }

    /**
     * 用于id查询
     * @param id id
     * @return 查询结果
     */
    @RequestMapping(value = "/find/{id}",method = RequestMethod.GET)
    public Response find(@PathVariable("id") long id) {
        logger.info(" invoke find/{id} {}" , id);
        DisinfectFilesModel disinfectFilesModel = this.disinfectFilesService.getDisinfectFilesModelById(id);
        return JudgeUtil.JudgeFind(disinfectFilesModel);
    }

//    /**
//     * 查看某专家负责的工厂
//     * @param agentId 代理ID
//     * @param factoryNum 工厂号
//     * @param page 页号
//     * @param size 页数
//     * @return 查询结果
//     */
//    @RequestMapping(value = "/professor",method = RequestMethod.GET)
//    public Response pFind(@RequestParam("agentId") Long agentId,
//                          @RequestParam(value = "factoryNum",required = false)BigInteger factoryNum,
//                          @RequestParam(value = "ispassCheck",required = false)String ispassCheck,
//                          @RequestParam(value = "page" , defaultValue = "0") int page,
//                          @RequestParam(value = "size" , defaultValue = "10") int size) {
//        logger.info(" invoke pFind{agentId, factoryNum, ispassCheck ,page ,size} {}" , agentId, factoryNum, ispassCheck, page ,size);
//        List<DisinfectFilesModel> list = new ArrayList<>();
//        if (factoryNum == null) {
//            //未指定factoryNum 查询出负责的所有的factoryID
//            long[] factoryId = AgentUtil.getFactory(agentId.toString());
//            if (factoryId == null) {
//                return Responses.errorResponse("find no factory");
//            }
//            for (long factory : factoryId) {
//                list.addAll(this.disinfectFilesService.getDisinfectFilesModelByFactoryNumAndIsPassCheck(BigInteger.valueOf(factory), ispassCheck, new RowBounds(page * size ,size)));
//            }
//            return JudgeUtil.JudgeFind(list , list.size());
//            //指定查询的factoryNum
//        } else {
//            System.out.println("11");
//            list.addAll(this.disinfectFilesService.getDisinfectFilesModelByFactoryNumAndIsPassCheck(factoryNum, ispassCheck, new RowBounds(page * size ,size)));
//            return JudgeUtil.JudgeFind(list , list.size());
//        }
//
//    }

//    /**
//     * 查看某监督员负责的工厂
//     * @param factoryNum 工厂号
//     * @param ispassSup  审核
//     * @param page  页
//     * @param size  条
//     * @return  查询结果
//     */
//    @RequestMapping(value = "/supervisor",method = RequestMethod.GET)
//    public Response sFind(@RequestParam(value = "factoryNum")BigInteger factoryNum,
//                          @RequestParam(value = "ispassSup",required = false)String ispassSup,
//                          @RequestParam(value = "page" , defaultValue = "0") int page,
//                          @RequestParam(value = "size" , defaultValue = "10") int size){
//        logger.info(" invoke sFind{factoryNum, ispassSup, page, size } {}" , factoryNum, ispassSup, page ,size);
//        List<DisinfectFilesModel> list = this.disinfectFilesService.getDisinfectFilesModelByFactoryNumAndIsPassSup(factoryNum, ispassSup, new RowBounds(page * size , size));
//        return JudgeUtil.JudgeFind(list, list.size());
//    }





    /**
     * 下载文件 并保存到自定义路径
     * @param response  HttpServletResponse
     * @param factoryNum  下载文件所属工厂号
     * @param fileName 文件名
     * @return  下载结果
     */
    @RequestMapping(value = "/down/{factoryNum}/{fileName}",method = RequestMethod.GET)
    public Response download(HttpServletResponse response,
                             @PathVariable("factoryNum") String factoryNum,
                             @PathVariable("fileName") String fileName) throws Exception {
        logger.info("invoke download {}", response, factoryNum, fileName);
        String filePath = pathPre + factoryNum + "/disinfectEartag/";
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        if (DownloadUtil.testDownload(response, filePath, fileName, outputStream)) {
            outputStream.close();
            return JudgeUtil.JudgeSuccess("download","Success");
        } else {
            return Responses.errorResponse("download Error");
        }
    }

    /**
     * 审核入口 IspassCheck = 0的数据
     * METHOD:PATCH
     * @param disinfectRequest 消毒类
     * @return 更新结果
     */
    @RequestMapping(value = "/p/{id}",method = RequestMethod.PATCH)
    public Response professorUpdate(@PathVariable(value = "id") int id,
                                    @RequestBody DisinfectRequest disinfectRequest,
                                    HttpServletRequest httpServletRequest) {
        disinfectRequest.setId(id);
        logger.info("invoke gf/p/{} {}",id, disinfectRequest);

        if( disinfectRequest.getIspassCheck() == null
            || disinfectRequest.getUnpassReason() == null) {
            return Responses.errorResponse("Lack Item");
        } else {
            int row = disinfectFilesService.updateDisinfectFilesModelByProfessor(disinfectRequest);
            if (row == 1) {
                String professorKey = this.factoryService.getAgentIDByFactoryNumber(disinfectRequest.getFactoryNum().toString()) + "_professor";
                if (!JedisUtil.redisCancelProfessorSupervisorWorks(professorKey)) {
                    return Responses.errorResponse("cancel error");
                }
            }
            return JudgeUtil.JudgeUpdate(row);
        }

    }

    /**
     * 监督员入口 审核isPass1 = 0的数据
     * 审核要求:审核时要求条例写完整 审核后 isPass = 1时 无权限再修改
     * @param disinfectRequest 消毒类
     * METHOD:PATCH
     * @return 审核结果
     */
    @PatchMapping(value = "/s/{id}")
    public Response supervisorUpdate(@PathVariable(value = "id") int id,
                                     @RequestBody DisinfectRequest disinfectRequest) {

        disinfectRequest.setId(id);
        logger.info("invoke supervisorUpdate {}", disinfectRequest);
        if ( disinfectRequest.getFactoryNum() == null
            || disinfectRequest.getSupervisor() == null
            || disinfectRequest.getIspassSup() == null) {
            return Responses.errorResponse("Lack Item");
        } else {
            int row = disinfectFilesService.updateDisinfectFilesModelBySupervisor(disinfectRequest);
            if (row == 1) {
                String supervisorKey = disinfectRequest.getFactoryNum().toString() + "_supervisor";
                if (!JedisUtil.redisCancelProfessorSupervisorWorks(supervisorKey)){
                    return Responses.errorResponse("cancel error");
                }
            }
            return JudgeUtil.JudgeUpdate(row);
        }
    }

    /**
     * 操作员在审核前想修改数据的接口
     * 或处理被退回操作的接口
     *
     * 行为1 与redis数据库无关
     * 行为2 redis对应数据字段+1
     * @param disinfectFilesModel 消毒类
     * @return 更新结果
     */
    @PostMapping(value = "/{id}")
    public Response operatorUpdate(@PathVariable(value = "id")long id,
                                   @Valid DisinfectFilesModel disinfectFilesModel,
                                   BindingResult bindingResult,
                                   @RequestParam(value = "eartagFile", required = false) MultipartFile disinfectEartagFile) {

        if (bindingResult.hasErrors()) {
            Response response = Responses.errorResponse("param is valid");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("param",bindingResult.getAllErrors());
            response.setData(data);
            return response;
        }

        logger.info("invoke operatorUpdate {}", disinfectFilesModel);
        disinfectFilesModel.setId(id);
        if (disinfectEartagFile != null) {
            String filePath = pathPre + disinfectFilesModel.getFactoryNum().toString() + "/disinfectEartag/";
            String fileName = disinfectEartagFile.getOriginalFilename();
            try {
                fileName =  UploadUtil.uploadFile(disinfectEartagFile.getBytes(),filePath,fileName);
            } catch (Exception e) {
                return Responses.errorResponse("update file error");
            }

            String oldPath = filePath + disinfectFilesModel.getDisinfectEartag();
            disinfectFilesModel.setDisinfectEartag(fileName);
            int row = this.disinfectFilesService.updateDisinfectFilesModelByOperatorName(disinfectFilesModel);
            if (row == 1) {
                File file = new File(oldPath);
                file.delete();
            } else {
                String newPath = filePath + fileName;
                File file = new File(newPath);
                file.delete();
            }
            return JudgeUtil.JudgeUpdate(row);
        } else {
            int row = this.disinfectFilesService.updateDisinfectFilesModelByOperatorName(disinfectFilesModel);
            return JudgeUtil.JudgeUpdate(row);
        }
    }

    /**
     * 删除对应ID数据
     * @param id id
     * @return 是否成功
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Response delete(@Min(0) @PathVariable(value = "id") Long id) {
        logger.info("invoke delete {}", id);
        if ("0".equals(id.toString())) {
            return Responses.errorResponse("Wrong id");
        }
        DisinfectFilesModel disinfectFilesModel = this.disinfectFilesService.getDisinfectFilesModelById(id);
        String filePath = pathPre + disinfectFilesModel.getFactoryNum() + "/disinfectEartag/"+disinfectFilesModel.getDisinfectEartag();
        int row = this.disinfectFilesService.deleteDisinfectFilesModelById(id);
        if (FileUtil.deleteFile(filePath) && row == 1){
            return JudgeUtil.JudgeDelete(row);
        } else {
            return Responses.errorResponse("delete wrong");
        }
    }

}

