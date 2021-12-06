package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequire;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.CookieUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("checkCart")
    public String checkCart(ModelMap map,HttpServletRequest request,HttpServletResponse response, String skuId,String isCheckedFlag){

        String userId= (String) request.getAttribute("userId");

        List<CartInfo> cartInfos = new ArrayList<>();
        //修改购物车状态
        if(StringUtils.isNotBlank(userId)){
            //改Db
            cartInfos = cartService.getCartListByUserId(userId);

        }else{
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            cartInfos = JSON.parseArray(cartListCookie,CartInfo.class);

        }

        for (CartInfo cartInfo : cartInfos) {
            if(cartInfo.getSkuId().equals(skuId)){
                cartInfo.setIsChecked(isCheckedFlag);
                if(StringUtils.isNotBlank(userId)){
                    cartService.updateCart(cartInfo);
                }
            }
        }
        if(StringUtils.isNotBlank(userId)){
            //更新Db

            //刷新缓存
            cartService.flushCache(userId);
        }else{
            //更新cookies
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartInfos),
                    60*60*24,true);
        }

        //刷新最新的列表
        map.put("cartList",cartInfos);
        map.put("totalPrice",getCartSum(cartInfos));
        return "cartListInner";
    }

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request, ModelMap map){

        List<CartInfo> cartInfos = new ArrayList<>();

        //判断用户是否登录
        String userId= (String) request.getAttribute("userId");
        if (StringUtils.isBlank(userId)){
            //取cookies中的数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)){
                cartInfos = JSON.parseArray(cartListCookie,CartInfo.class);
            }
        }else{
            //取缓存中的数据
            cartInfos = cartService.getCartListFromCache(userId);
        }
        map.put("cartList",cartInfos);
        BigDecimal totalPrice=getCartSum(cartInfos);
        map.put("totalPrice",totalPrice);
        return "cartList";
    }

    private BigDecimal getCartSum(List<CartInfo> cartInfos) {

        //计算购物车中被选中的总价格
        BigDecimal sum = new BigDecimal(0);
        for (CartInfo cartInfo : cartInfos) {
            if(cartInfo.getIsChecked().equals("1")){
                sum = sum.add(cartInfo.getCartPrice());
            }
        }

        return sum;
    }

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("addToCart")
    public String addToCart(HttpServletRequest request, HttpServletResponse response, String skuId, int num){
        //购物车添加逻辑
        SkuInfo skuInfo = skuService.getSkuById(skuId, "0.0.0.0");
        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuPrice(skuInfo.getPrice());
        cartInfo.setSkuNum(num);
        cartInfo.setCartPrice(cartInfo.getSkuPrice().multiply(new BigDecimal(cartInfo.getSkuNum())));

        cartInfo.setIsChecked("1");
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setSkuId(skuInfo.getId());
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        //获取用户id
        String userId= (String) request.getAttribute("userId");
        List<CartInfo> cartInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(userId)){
            //用户已登录
            cartInfo.setUserId(userId);
            CartInfo cartInfoDb = cartService.selectCartExists(cartInfo);
            if(null==cartInfoDb){
                //Db中没有购物车信息，需要插入
                cartService.addCart(cartInfo);
            }else{
                //更新购物车

                cartInfoDb.setSkuNum(cartInfoDb.getSkuNum()+num);
                cartInfoDb.setCartPrice(cartInfoDb.getSkuPrice().multiply(new BigDecimal(cartInfoDb.getSkuNum())));

                cartService.updateCart(cartInfoDb);
            }
            //同步购物车缓存
            cartService.flushCache(userId);
        }else{
            //用户没有登录

            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isBlank(cartListCookie)){
                // 将购物车集合放入cookie
                cartInfos.add(cartInfo);

            }else{
                cartInfos = JSON.parseArray(cartListCookie, CartInfo.class);
                boolean updatetag = false;
                for (CartInfo info : cartInfos) {
                    if (skuId.equals(info.getSkuId())){
                        //更新
                        info.setSkuNum(info.getSkuNum()+num);
                        info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                        updatetag=true;
                        break;
                    }
                }
                //如果购物车中不存在相同物品，进行添加
                if(!updatetag){
                    cartInfos.add(cartInfo);
                }
            }
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartInfos),
                    60*60*24,true);
        }

        return "redirect:/success.html";
    }

    @LoginRequire(isNeededSuccess = false)
    @RequestMapping("cartAddSuccess")
    public String cartAddSuccess(){

        return "success";
    }
}
