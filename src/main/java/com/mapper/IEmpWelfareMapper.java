package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.EmpWelfare;
import com.po.Welfare;

@Service("EmpWelfareDAO")
public interface IEmpWelfareMapper {
   //����Ա���븣��
	public int save(EmpWelfare ewf);
   //����Ա����ţ�ɾ����������
	public int delByEid(Integer eid);
   //����Ա����ţ���ѯ��������
	public List<Welfare> findByEid(Integer eid);
	
}
