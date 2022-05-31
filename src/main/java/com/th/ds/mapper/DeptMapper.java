package com.th.ds.mapper;

import com.th.ds.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author th
 * @since 2022-05-30
 */

public interface DeptMapper extends BaseMapper<Dept> {

    List<Dept> getAllDepts(Dept dept);

}
