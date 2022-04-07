package com.mapper;

import org.springframework.stereotype.Service;

import com.po.Salary;

@Service("SalaryDAO")
public interface ISalaryMapper {
   //����н��
	public int save(Salary sa);
   //ͨ��Ա��Id���޸�н��
	public int updateByEid(Salary sa);
   //ͨ��Ա��Id��ɾ��н��
	public int delByEid(Integer eid);
   //ͨ��Ա��Id����ѯн��
	public Salary findByEid(Integer eid);
}
