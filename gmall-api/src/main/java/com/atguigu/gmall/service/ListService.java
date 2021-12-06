package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParam;

import java.io.IOException;
import java.util.List;

public interface ListService {

    List<SkuLsInfo> search(SkuLsParam skuLsParam) throws IOException;
}
