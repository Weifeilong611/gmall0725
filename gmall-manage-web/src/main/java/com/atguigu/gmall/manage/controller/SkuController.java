package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.BaseAttrService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    @Reference
    BaseAttrService baseAttrService;

    @RequestMapping("skuInfoListBySpu")
    @ResponseBody
    public List<SkuInfo> getAttrList(String spuId){
        //根据spuId获取sku列表
        List<SkuInfo> skuInfos = skuService.skuInfoListBySpu(spuId);
        return skuInfos;
    }

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        //根据spuId获取sku列表

        List<BaseAttrInfo> baseAttrInfos = baseAttrService.attrInfoList(catalog3Id);
        return baseAttrInfos;
    }

    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        //根据spuId获取sku列表

        List<SpuSaleAttr> spuSaleAttrs= spuService.spuSaleAttrList(spuId);
        return spuSaleAttrs;
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public List<SpuImage> spuImageList(String spuId){
        //根据spuId获取sku列表

        List<SpuImage> spuImages= spuService.spuImageList(spuId);
        return spuImages;
    }

    @RequestMapping("saveSku")
    @ResponseBody
    public String saveSku(SkuInfo skuInfo){

        //保存sku
        skuService.saveSku(skuInfo);
        return "success";
    }
}
