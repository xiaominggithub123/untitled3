package com.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Dep;
import com.po.Emp;
import com.po.EmpWelfare;
import com.po.PageBean;
import com.po.Salary;
import com.po.Welfare;
import com.service.*;
import com.util.DaoService;
@Service("EmpService")
@Transactional
public class EmpServiceImpl implements IEmpService {
	@Resource(name="DaoService")
    private DaoService daoService;
	
	public DaoService getDaoService() {
		return daoService;
	}

	public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}

	@Override
	public int save(Emp emp) {
		int code=daoService.getEmpMapper().save(emp);
		//���Ա��������ɣ���Ҫ��ȡԱ��Id���洢н�ʺ͸���
		  //1.��ȡԱ�������(�ղŴ���ı��)
		Integer eid=daoService.getEmpMapper().findMaxEid();
		/******����н��********/
		Salary sa=new Salary(eid,emp.getEmoney());
		daoService.getSalaryMapper().save(sa);
		/******����н��end********/
		/******���渣����Ա��ѡȡ�ĸ���Id��********/
		String[] wids=emp.getWids();
		if(wids!=null && wids.length>0){
			for (int i = 0; i < wids.length; i++) {
				EmpWelfare ewf=new EmpWelfare(eid,Integer.parseInt(wids[i]));
				daoService.getEmpWelfareMapper().save(ewf);
			}
		}
		/******���渣��end********/
		return code;
	}

	@Override
	public int update(Emp emp) {
		int code=daoService.getEmpMapper().update(emp);
		/******����н��********/
		  //1.��ȡԭ��н��
		  Salary oldsa=daoService.getSalaryMapper().findByEid(emp.getEid());
		  if(oldsa!=null&&oldsa.getEmoney()!=null){//ԭ����н��
			  oldsa.setEmoney(emp.getEmoney());
			  daoService.getSalaryMapper().updateByEid(oldsa);
		  }else{//ԭ��û��н��
			    Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoService.getSalaryMapper().save(sa);
		  }
		/******����н��end********/
		  /******���¸�����Ա��ѡȡ�ĸ���Id��********/
		     //1.��ȡԭ�и���
		   List<Welfare> lswf=daoService.getEmpWelfareMapper().findByEid(emp.getEid());
		   if(lswf!=null&&lswf.size()>0){
			   //ɾ��ԭ�и���
			   daoService.getEmpWelfareMapper().delByEid(emp.getEid());
		   }
		   String[] wids=emp.getWids();
			if(wids!=null && wids.length>0){
				for (int i = 0; i < wids.length; i++) {
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),Integer.parseInt(wids[i]));
					daoService.getEmpWelfareMapper().save(ewf);
				}
			}
		  /******���¸���end********/
		return code;
	}

	@Override
	public int delById(Integer eid) {
		//��ɾ�ӱ�
		daoService.getSalaryMapper().delByEid(eid);
		daoService.getEmpWelfareMapper().delByEid(eid);
		int code=daoService.getEmpMapper().delById(eid);
		return code;
	}

	@Override
	public Emp findById(Integer eid) {
		System.out.println(eid);
		//��ȡԱ��
		Emp emp=daoService.getEmpMapper().findById(eid);
		/**��ȡн��**/
		System.out.println("aaa"+emp.toString());
		Salary sa=daoService.getSalaryMapper().findByEid(eid);
		System.out.println(sa.toString());
		if(sa!=null&&sa.getEmoney()!=null){
			emp.setEmoney(sa.getEmoney());
		}
		/**��ȡн��end**/
		/**��ȡ����**/
		List<Welfare> lswf=daoService.getEmpWelfareMapper().findByEid(eid);
		System.out.println(lswf.toString());
		if(lswf!=null&&lswf.size()>0){
			String[] wids=new String[lswf.size()];
			for (int i = 0; i < lswf.size(); i++) {
				Welfare wa=lswf.get(i);
				wids[i]=wa.getWid().toString();
			}
			emp.setWids(wids);
		}
		emp.setLswf(lswf);
		/**��ȡ����end**/
		return emp;
	}

	@Override
	public List<Emp> findPageAll(PageBean pb) {
      if(pb==null){
    	  pb=new PageBean();
      }
		return daoService.getEmpMapper().findPageAll(pb);
	}

	@Override
	public int findMaxRow() {
		// TODO Auto-generated method stub
		return daoService.getEmpMapper().findMaxRow();
	}

	

}
