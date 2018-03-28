package com.deep.api.resource;

import com.deep.api.response.Response;
import com.deep.api.response.Responses;
import com.deep.domain.model.NoticePlan;
import com.deep.domain.model.NoticePlanExample;
import com.deep.domain.model.OtherTime;
import com.deep.domain.service.NoticePlanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * author: Created  By  Caojiawei
 * date: 2018/3/8  20:14
 */
@Controller
public class NoticeResource {
    @Resource
    private NoticePlanService noticePlanService;

    @ResponseBody
    @RequestMapping(value = "/noticePlan",method = RequestMethod.GET)
    public String helloNotice() {
        return "Hello NoticePlan!";
    }

//    按主键删除的接口：/noticeInsert
//    按主键删除的方法名：addPlan()
//    接收参数：整个表单信息（所有参数必填）
//    参数类型为：String professor;Byte type;String title;String content;
    @RequestMapping(value = "/noticeInsert",method = RequestMethod.GET)
    public String addPlan(){
        return "NoticeInsert";
    }
    @ResponseBody
    @RequestMapping(value = "/noticeInsert/show",method = RequestMethod.POST)
    public Response addPlan(@Valid NoticePlan insert, HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filepath = request.getSession().getServletContext().getContextPath()+"../"+"picture/"+insert.getProfessor()+"/";
        MultipartFile file = files.get(0);
        String filename = file.getOriginalFilename();
        String suffixname = "";
        if (!filename.isEmpty()){
            suffixname = filename.substring(filename.lastIndexOf("."));
        }
        if (!file.isEmpty()) {
            try {
                noticePlanService.uploadFile(file.getBytes(), filepath, filename);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        insert.setGmtCreate(new Date());
        insert.setProfessor(insert.getProfessor());
        insert.setType(insert.getType());
        insert.setTitle(insert.getTitle());
        if (!filename.isEmpty()){
            insert.setFilepath(filepath+filename);
        }else {
            insert.setFilepath(null);
        }
        if (!filename.isEmpty()){
            insert.setSuffixname(suffixname);
        }else {
            insert.setSuffixname(null);
        }
        insert.setContent(insert.getContent());
        noticePlanService.addPlan(insert);

        NoticePlanExample insertExample = new NoticePlanExample();
        NoticePlanExample.Criteria criteria = insertExample.createCriteria();
        criteria.andTitleEqualTo(insert.getTitle());
        criteria.andTypeEqualTo(insert.getType());
        criteria.andProfessorEqualTo(insert.getProfessor());
        if (!filename.isEmpty()){
            criteria.andFilepathEqualTo(filepath+filename);
            criteria.andSuffixnameEqualTo(suffixname);
        }
        List<NoticePlan> select = noticePlanService.findPlanSelective(insertExample);
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("notice_plan",select);
        response.setData(data);
        return response;
    }

//    按主键删除的接口：/noticeDeleteById
//    按主键删除的方法名：dropPlan()
//    接收参数：整型id，根据主键号删除
    @RequestMapping(value = "/noticeDeleteById",method = RequestMethod.GET)
    public String dropPlan(){
        return "NoticeDeleteById";
    }
    @ResponseBody
        @RequestMapping(value = "/noticeDeleteById/show",method = RequestMethod.DELETE)
    public Response dropPlan(@RequestBody NoticePlan delete){
        noticePlanService.dropPlan(delete.getId());
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("notice_plan",delete);
        response.setData(data);
        return response;
    }

//    按主键修改的接口：/noticeUpdate
//    按主键修改的方法名：changePlan()
//    接收参数：整个表单信息（整型id必填，各参数选填）
    @RequestMapping(value = "/noticeUpdate",method = RequestMethod.GET)
    public String changePlan(){
        return "NoticeUpdate";
    }
    @ResponseBody
    @RequestMapping(value = "/noticeUpdate/show",method = RequestMethod.PUT)
    public Response changePlan(@RequestBody NoticePlan update,HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = files.get(0);
        String filename = file.getOriginalFilename();
        String suffixname = "";
        String filepath = request.getSession().getServletContext().getContextPath()+"../picture/";
        if (!update.getProfessor().isEmpty()){
            filepath = request.getSession().getServletContext().getContextPath()+"../picture/"+update.getProfessor()+"/";
        }
        if (!filename.isEmpty()){
            suffixname = filename.substring(filename.lastIndexOf("."));
        }
        if (!file.isEmpty()) {
            try {
                noticePlanService.uploadFile(file.getBytes(), filepath, filename);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        update.setId(update.getId());
        update.setGmtModified(new Date());
        update.setType(update.getType());
        update.setTitle(update.getTitle());
        if (!filename.isEmpty()){
            update.setFilepath(filepath+filename);
        }else {
            update.setFilepath(null);
        }
        if (!filename.isEmpty()){
            update.setSuffixname(suffixname);
        }else {
            update.setSuffixname(null);
        }
        update.setContent(update.getContent());
        noticePlanService.changePlan(update);
        NoticePlan selectById = noticePlanService.findPlanById(update.getId());

        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("notice_plan",selectById);
        response.setData(data);
        return response;
    }

//    按主键查询的接口：/noticeSelectById
//    按主键查询的方法名：findPlanById()
//    接收参数：整型的主键号（保留接口查询，前端不调用此接口）
    @RequestMapping(value = "/noticeSelectById",method = RequestMethod.GET)
    public String findPlanById(){
        return "NoticeSelectById";
    }
    @ResponseBody
    @RequestMapping(value = "/noticeSelectById/show",method = RequestMethod.GET)
    public Response findPlanById(@RequestParam("id") Integer id){
        NoticePlan selectById = noticePlanService.findPlanById(id);
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("notice_plan",selectById);
        response.setData(data);
        return response;
    }

//    按条件查询接口：/noticeSelective
//    按条件查询方法名：findPlanSelective()
//    接收的参数：前端的各参数，以及四个时间字符串（所有参数可以选填）
    @RequestMapping(value = "/noticeSelective",method = RequestMethod.GET)
    public String findPlanSelective(){
        return "NoticeSelective";
    }
    @ResponseBody
    @RequestMapping(value = "/noticeSelective/show",method = RequestMethod.GET)
    public Response findPlanSelective(@Valid NoticePlan noticePlan, @Valid OtherTime otherTime) throws ParseException{
        Date gmtCreate1 = null;
        Date gmtCreate2 = null;
        Date gmtModified1 = null;
        Date gmtModified2 = null;
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS");
        NoticePlanExample noticePlanExample = new NoticePlanExample();
        NoticePlanExample.Criteria criteria = noticePlanExample.createCriteria();
        if (otherTime.getS_gmtCreate1() != null && otherTime.getS_gmtCreate1() != "" && otherTime.getS_gmtCreate2() != null && otherTime.getS_gmtCreate2() != ""){
            gmtCreate1 =  formatter.parse(otherTime.getS_gmtCreate1());
            gmtCreate2 =  formatter.parse(otherTime.getS_gmtCreate2());
        }
        if (otherTime.getS_gmtModified1() != null && otherTime.getS_gmtModified1() != "" && otherTime.getS_gmtModified2() != null && otherTime.getS_gmtModified2() != ""){
            gmtModified1 =  formatter.parse(otherTime.getS_gmtModified1());
            gmtModified2 =  formatter.parse(otherTime.getS_gmtModified2());
        }
        if(noticePlan.getGmtCreate() != null && noticePlan.getGmtCreate().toString() !=""){
            criteria.andGmtCreateBetween(gmtCreate1,gmtCreate2);
        }
        if(noticePlan.getGmtModified() != null && noticePlan.getGmtModified().toString() !=""){
            criteria.andGmtModifiedBetween(gmtModified1,gmtModified2);
        }
        if(noticePlan.getId() != null && noticePlan.getId().toString() !=""){
            criteria.andIdEqualTo(noticePlan.getId());
        }
        if(noticePlan.getProfessor() != null && noticePlan.getProfessor() !=""){
            criteria.andProfessorEqualTo(noticePlan.getProfessor());
        }
        if(noticePlan.getType() != null && noticePlan.getType().toString() !=""){
            criteria.andTypeEqualTo(noticePlan.getType());
        }
        List<NoticePlan> selective = noticePlanService.findPlanSelective(noticePlanExample);
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("notice_plan",selective);
        response.setData(data);
        return response;
    }

//    站内搜索接口：/searchInSite
//    站内搜索方法名：searchInSite()
//    接收的参数：用户在搜索栏输入的信息（字符串）
    @RequestMapping(value = "/searchInSite",method = RequestMethod.GET)
    public String searchInSite(){
        return "SearchInSite";
    }
    @ResponseBody
    @RequestMapping(value = "/searchInSite/show",method = RequestMethod.GET)
    public Response searchInSite(@Valid OtherTime otherTime){
        List<NoticePlan> selectInSite = noticePlanService.selectInSite(otherTime.getSearch_string());
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("notice_plan",selectInSite);
        response.setData(data);
        return response;
    }

//    上传接口：/upload
//    上传方法名：uploadFile()
//    接收的参数：用户浏览本地文件选择文件上传
    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String uploadFile(){
        return "Upload";
    }
    @ResponseBody
    @RequestMapping(value = "/upload/show",method = RequestMethod.POST)
    public Response uploadFile(@RequestBody NoticePlan noticePlan,
                               HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filepath = request.getSession().getServletContext().getContextPath()+"../picture/rich_text_format";
        List<String> path = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String filename = file.getOriginalFilename();
            if (!file.isEmpty()) {
                try {
                    noticePlanService.uploadFile(file.getBytes(), filepath, filename);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            path.add(filepath+filename);
        }
        Response response = Responses.successResponse();
        HashMap<String, Object> data = new HashMap<>();
        data.put("address",path);
        response.setData(data);
        return response;
    }

//    下载接口：/download
//    下载方法名：downloadFile()
//    接收的参数：文件在服务器的相对路径
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public String downloadFile(){
        return "Download";
    }
    @ResponseBody
    @RequestMapping(value = "/download/show",method = RequestMethod.GET)
    public String downloadFile(@RequestParam ("filePath") String filePath,
                               HttpServletResponse response){
            File file = new File(filePath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
//                response.addHeader("Content-Disposition", "attachment;file=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        return null;
    }
}
