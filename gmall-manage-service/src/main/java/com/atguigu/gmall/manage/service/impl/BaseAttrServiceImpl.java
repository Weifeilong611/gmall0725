package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.manage.mapper.BaseAttrInfoValueMapper;
import com.atguigu.gmall.manage.mapper.BaseAttrMapper;
import com.atguigu.gmall.service.BaseAttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
public class BaseAttrServiceImpl implements BaseAttrService {

    @Autowired
    BaseAttrMapper baseAttrMapper;

    @Autowired
    BaseAttrInfoValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrMapper.select(baseAttrInfo);
        return select;
    }

    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {
        String id = baseAttrInfo.getId();
        if (StringUtils.isBlank(id)){
            baseAttrMapper.insertSelective(baseAttrInfo);
            String attrId = baseAttrInfo.getId();
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(id);
                baseAttrValueMapper.insertSelective(baseAttrValue);
            }
        }else{
            //更新
        }

    }

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        //查询属性基本信息
        BaseAttrInfo baseAttrInfo = baseAttrMapper.selectByPrimaryKey(attrId);

        //查询属性对应的属性值
        BaseAttrValue  baseAttrValue4Query =new BaseAttrValue();
        baseAttrValue4Query.setAttrId(baseAttrInfo.getId());
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue4Query);

        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;

    }
}
