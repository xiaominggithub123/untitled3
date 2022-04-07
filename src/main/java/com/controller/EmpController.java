package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.util.AJAXUtil;
import com.util.BizService;
@Controller
public class EmpController implements IEmpController {
	@Resource(name="bizService")
   private BizService bizService;
	
	public BizService getBizService() {
		return bizService;
	}

	public void setBizService(BizService bizService) {
		this.bizService = bizService;
	}

	@RequestMapping(value="save_Emp.do")
	public String save(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		//获取网络项目的根路径
				String realpath=request.getRealPath("/");
				/*******  文件上传   *********/
				   //获取文件上传对象
				MultipartFile multipartFile=emp.getPic();
				if(multipartFile!=null&&!multipartFile.isEmpty()){
					//获取上传文件的名称
					String fname=multipartFile.getOriginalFilename();
					 //更名
					if(fname.lastIndexOf(".")!=-1){
						//获取后缀
						String ext=fname.substring(fname.lastIndexOf("."));
						//限制格式
						if(ext.equalsIgnoreCase(".jpg")){
							String newfname=new Date().getTime()+ext;
							//创建文件将内容写入到新建文件
							File dostFile=new File(realpath+"/uppic/"+newfname);
							//上传
							try {
								FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
							    emp.setPhoto(newfname);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
				}
				/*******文件上传end*********/
		int flag=bizService.getEmpService().save(emp);
		if(flag>0){
			AJAXUtil.printString(response, "1");
		}else{
			AJAXUtil.printString(response, "0");
		}
		return null;
	}

	@RequestMapping(value="update_Emp.do")
	public String update(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		//获取原有文件对象
		String oldfname=bizService.getEmpService().findById(emp.getEid()).getPhoto();
		String realpath=request.getRealPath("/");
		/*******  文件上传   *********/
		   //获取文件上传对象
		MultipartFile multipartFile=emp.getPic();
		if(multipartFile!=null&&!multipartFile.isEmpty()){
			//获取上传文件的名称
			String fname=multipartFile.getOriginalFilename();
			 //更名
			if(fname.lastIndexOf(".")!=-1){
				//获取后缀
				String ext=fname.substring(fname.lastIndexOf("."));
				//限制格式
				if(ext.equalsIgnoreCase(".jpg")){
					String newfname=new Date().getTime()+ext;
					//创建文件将内容写入到新建文件
					File dostFile=new File(realpath+"/uppic/"+newfname);
					//上传
					try {
						FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostFile);
					   emp.setPhoto(newfname);
					    //删除原有照片
					    File oldfile=new File(realpath+"/uppic/"+oldfname);
						if(oldfile.exists()&&!oldfname.equals("default.jpg")){
							oldfile.delete();
						}
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}else{//没有修改文件上传
			emp.setPhoto(oldfname);
		}
		/*******文件上传end*********/
		int flag=bizService.getEmpService().update(emp);
		if(flag>0){
			AJAXUtil.printString(response, "1");
		}else{
			AJAXUtil.printString(response, "0");
		}
		return null;
	}

	@RequestMapping(value="delById_Emp.do")
	public String delById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		//获取原有文件对象
		String oldfname=bizService.getEmpService().findById(eid).getPhoto();
		String realpath=request.getRealPath("/");
		int flag=bizService.getEmpService().delById(eid);
		if(flag>0){
			File oldfile=new File(realpath+"/uppic/"+oldfname);
			if(oldfile.exists()&&!oldfname.equals("default.jpg")){
				oldfile.delete();
			}
			AJAXUtil.printString(response, "1");
		}else{
			AJAXUtil.printString(response, "0");
		}
		return null;
	}

	@RequestMapping(value="findById_Emp.do")
	public String findById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		Emp oldemp=bizService.getEmpService().findById(eid);
		System.out.println(oldemp.toString());
		String Jsonstr=JSONObject.toJSONString(oldemp);
		AJAXUtil.printString(response, Jsonstr);
		return null;
	}

	@RequestMapping(value="findDetail_Emp.do")
	public String findDetail(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		Emp oldemp=bizService.getEmpService().findById(eid);
		System.out.println(oldemp.toString());
		String Jsonstr=JSONObject.toJSONString(oldemp);
		AJAXUtil.printString(response, Jsonstr);
		return null;
	}

	@RequestMapping(value="findPageAll_Emp.do")
	public String findPageAll(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows) {
		//返回的数据，保证是easyui的格式
		Map<String, Object> map=new HashMap<String, Object>();
		PageBean pb=new PageBean();
		page=page==null || page<1?pb.getPage():page;
		rows=rows==null || rows<1?pb.getRows():rows;
		if(rows>10)rows=10;
		pb.setPage(page);
		pb.setRows(rows);
		List<Emp> lsemp=bizService.getEmpService().findPageAll(pb);
		int maxrows=bizService.getEmpService().findMaxRow();
		map.put("page", page);
		map.put("rows", lsemp);
		map.put("total",maxrows);
		String Jsonstr=JSONObject.toJSONString(map);
		AJAXUtil.printString(response, Jsonstr);
		return null;
	}

	@RequestMapping(value="doin_Emp.do")
	public String doint(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map=new HashMap<String,Object>();
		List<Dep> lsdep=bizService.getDepService().findAll();
		List<Welfare> lswf=bizService.getWelfareService().findAll();
		map.put("lsdep", lsdep);
		map.put("lswf", lswf);
		
		String Jsonstr=JSONObject.toJSONString(map);
		AJAXUtil.printString(response, Jsonstr);
		return null;
	}

}
