package com.deep.api.resource;

import com.deep.api.response.Response;
import com.deep.api.response.Responses;
import com.deep.api.response.ValidResponse;
import com.deep.domain.model.GenealogicalFilesModel;
import com.deep.domain.model.TypeBriefModel;
import com.deep.domain.service.GenealogicalFilesService;
import com.deep.domain.service.TypeBriefService;
import com.deep.domain.util.JedisUtil;
import com.deep.domain.util.JudgeUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/gf")
public class GenealogicalFilesResource {

    private Logger logger = LoggerFactory.getLogger(GenealogicalFilesResource.class);
    //替代注册bean
    @Resource
    private GenealogicalFilesService genealogicalFilesService;

    @Resource
    private TypeBriefService typeBriefService;

    /**
     * 用于存放羊品种以及简介
     * @param typeBriefModel
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/type",method = RequestMethod.POST)
    public Response type(@RequestBody @Validated TypeBriefModel typeBriefModel,
                         BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return ValidResponse.bindExceptionHandler();
        }else{
            //若数据库中已经存在
            TypeBriefModel typeBriefModel1;
            if ((typeBriefModel1 = this.typeBriefService.getTypeBrief(typeBriefModel.getType())) != null){
                //System.out.println(this.typeBriefService.getTypeBrief(typeBriefModel) != null);
                //通过id更新
                typeBriefModel.setId(typeBriefModel1.getId());
                int row = this.typeBriefService.updateTypeBrief(typeBriefModel);
                //System.out.println("row"+row);
                return JudgeUtil.JudgeUpdate(row);
            }else{
                this.typeBriefService.setTypeBrief(typeBriefModel);
            }

        }

        return Responses.successResponse();
    }

    /**
     * 查询之前 需要返回给前端的数据
     * 山羊品种对应的特征
     * @return
     */
    @RequestMapping(value = "/beforesave",method = RequestMethod.GET)
    public Response beforeSave(){

        return JudgeUtil.JudgeSuccess("type",this.typeBriefService.getAllType());
    }


    /**
     * 返回插入结果
     * 成功：success
     * 失败：返回对应失败错误
     *
     * color:棕色 0  暗红 1  杂色 2
     * sex:公 0 母 1
     * METHOD:POST
     * @param genealogicalFilesModel
     * @return
     */
    @RequestMapping(value = "/saveshow",method = RequestMethod.POST)
    public Response saveShow(@RequestBody @Validated GenealogicalFilesModel genealogicalFilesModel,
                             BindingResult bindingResult) {
      if (bindingResult.hasErrors()) {
          return ValidResponse.bindExceptionHandler();
      }
      logger.info("invoke saveShow {}",genealogicalFilesModel);
      //查看数据库中是否有这种羊的品种信息
      List<String> list = this.typeBriefService.getAllType();
      int i = 0;
      for (String tempType : list){
          //System.out.println("temp:"+tempType);
          //System.out.println("type:"+genealogicalFilesModel.getType());
          if (tempType.equals(genealogicalFilesModel.getType())){
              i = 1;
              break;
          }
      }
      if (i == 0){
          return Responses.errorResponse("No this type before");
      }
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String time = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
      genealogicalFilesModel.setGmtCreate(time);
      genealogicalFilesModel.setGmtModified(time);
      try{
          int id = genealogicalFilesService.insertGenealogicalFilesModel(genealogicalFilesModel);
          if (id == 0) {
              return Responses.errorResponse("add data error");
          } else {
              HashMap<String,Object> data = new HashMap<>();
              data.put("id",genealogicalFilesModel.getId());
              return Responses.successResponse(data);
          }
      }catch (Exception e){
          e.printStackTrace();
          return Responses.errorResponse("data already exist");
      }

    }

    /**
     * 用于条件查找
     * RowBounds为必传参数
     * METHOD:POST
     * @param genealogicalFilesModel
     * @return
     */
    //bound为必传参数
    @RequestMapping(value = "/findshow",method = RequestMethod.POST)
    public Response findShow(@RequestBody GenealogicalFilesModel genealogicalFilesModel){

        logger.info("invoke findShow {}",genealogicalFilesModel);

        if ( genealogicalFilesModel.getPage() == 0 ) {
            genealogicalFilesModel.setPage(0);
        }

        if ( genealogicalFilesModel.getSize() == 0 ) {
            genealogicalFilesModel.setSize(10);
        }

        List<GenealogicalFilesModel> genealogicalFilesModels = genealogicalFilesService.getGenealogicalFilesModel(genealogicalFilesModel,new RowBounds(genealogicalFilesModel.getPage(),genealogicalFilesModel.getSize()));
        for(int i = 0 ; i < genealogicalFilesModels.size() ; i ++){
            String brief = this.typeBriefService.getTypeBrief(genealogicalFilesModels.get(i).getType()).getBrief();
            genealogicalFilesModels.get(i).setBrief(brief);
        }
        return JudgeUtil.JudgeFind(genealogicalFilesModels,genealogicalFilesModels.size());
    }

    /**
     * 查询出满足若干条件后的结果
     * 可由id进行操作
     * METHOD:GET
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findshowbyid",method = RequestMethod.GET)

    public Response findShowById(@RequestParam("id") int id ) {

        logger.info("invoke findShowById {}", id);


        GenealogicalFilesModel genealogicalFilesModel = genealogicalFilesService.getGenealogicalFilesModelByid(id);

        return JudgeUtil.JudgeFind(genealogicalFilesModel);

    }

    //update

    /**
     * 更新操作 输入数据替代原数据
     * METHOD:PATCH
     * @param genealogicalFilesModel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.PATCH)
    public Response update(@RequestBody GenealogicalFilesModel genealogicalFilesModel){

        logger.info("invoke update {}", genealogicalFilesModel);

        if (genealogicalFilesService.getGenealogicalFilesModelByNativeEartag(genealogicalFilesModel.getNativeEartag()) == null
            && genealogicalFilesService.getGenealogicalFilesModelByimmuneEartag(genealogicalFilesModel.getImmuneEartag()) == null
            && genealogicalFilesService.getGenealogicalFilesModelBytradeMarkEartag(genealogicalFilesModel.getTradeMarkEartag()) == null) {
            int row = genealogicalFilesService.updateGenealogicalFilesModel(genealogicalFilesModel);
            return JudgeUtil.JudgeUpdate(row);
        }else {
            return Responses.errorResponse("conflict with existing datas");
        }

    }

    /**
     * 返回删除内容行号
     * METHOD:DELETE
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)

    public Response delete(@RequestParam("id") int id) {
        logger.info("invoke delete {}", id);
        int row = genealogicalFilesService.deleteGenealogicalFilesModel(id);
        return JudgeUtil.JudgeDelete(row);
    }

}
