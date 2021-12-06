package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequire;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.OrderDetail;
import com.atguigu.gmall.bean.OrderInfo;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.enums.PaymentWay;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class OrderController {

    @Reference
    UserService userService;
    @Reference
    OrderService orderService;
    @Reference
    CartService cartService;

    @LoginRequire(isNeededSuccess = true)
    @RequestMapping("submitOrder")
    public String submitOrder(HttpServletRequest request, ModelMap map, OrderInfo orderInfo,String tradeCode,String addressId) {
        String userId= (String) request.getAttribute("userId");
        String nickName = (String)request.getAttribute("nickName");

        //防止订单重复提交
        boolean b = orderService.checkTradeCode(userId,tradeCode);
        if(b){

            //需要被删除的购物车集合
            List<String> delList = new ArrayList<>();

            //生成订单和订单详情数据，Db
            List<CartInfo> cartListByUserId = cartService.getCartListByUserId(userId);
            UserAddress userAddress = userService.getAddressListById(addressId);
            OrderInfo orderInfoDb = new OrderInfo();
            //外部订单号
            //atguigugmall+毫秒时间戳字符串—+订单生成的时间字符串
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(new Date());
            orderInfoDb.setOutTradeNo("atguigugmall"+System.currentTimeMillis()+format);
            orderInfoDb.setOrderStatus("订单已提交");
            orderInfoDb.setProcessStatus("订单已提交");
            orderInfoDb.setConsignee(orderInfo.getConsignee());
            //获取订单的过期时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,1);
            orderInfoDb.setExpireTime(calendar.getTime());
            orderInfoDb.setCreateTime(new Date());
            orderInfoDb.setDeliveryAddress(userAddress.getUserAddress());
            orderInfoDb.setConsigneeTel(userAddress.getPhoneNum());
            orderInfoDb.setOrderComment("硅谷订单");
            orderInfoDb.setTotalAmount(getCartSum(cartListByUserId));
            orderInfoDb.setPaymentWay(PaymentWay.ONLINE);
            orderInfoDb.setUserId(userId);

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (CartInfo cartInfo : cartListByUserId) {
                if(cartInfo.getIsChecked().equals("1")) {

                    //验价

                    //验库存


                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setSkuNum(cartInfo.getSkuNum());
                    orderDetail.setSkuName(cartInfo.getSkuName());
                    orderDetail.setImgUrl(cartInfo.getImgUrl());
                    orderDetail.setSkuId(cartInfo.getSkuId());
                    orderDetail.setOrderPrice(cartInfo.getCartPrice());

                    orderDetails.add(orderDetail);
                    delList.add(cartInfo.getId());
                }
            }
            orderInfoDb.setOrderDetailList(orderDetails);

            orderService.saveOrder(orderInfoDb);
//            System.out.println(userAddress);

            //删除购物车数据
//            orderService.deleteCheckedCart(delList);
            // 刷新购物车缓存
            cartService.flushCache(userId);

            //重定向到支付页面
            return "redirect:http://payment.gmall.com:8090/index?outTradeNo="+orderInfoDb.getOutTradeNo()+"&totalAmount="+orderInfoDb.getTotalAmount();
        }else {
            return "tradeFail";
        }


    }

    @LoginRequire(isNeededSuccess = true)
    @RequestMapping("toTrade")
    public String toTrade(HttpServletRequest request, ModelMap map){
        String userId= (String) request.getAttribute("userId");
        String nickName = (String)request.getAttribute("nickName");

        List<UserAddress> addressListByUserId = userService.getAddressListByUserId(userId);

        List<CartInfo> cartListByUserId = cartService.getCartListByUserId(userId);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartInfo cartInfo : cartListByUserId) {
            if(cartInfo.getIsChecked().equals("1")) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setSkuNum(cartInfo.getSkuNum());
                orderDetail.setSkuName(cartInfo.getSkuName());
                orderDetail.setImgUrl(cartInfo.getImgUrl());
                orderDetail.setSkuId(cartInfo.getSkuId());
                orderDetail.setOrderPrice(cartInfo.getCartPrice());

                orderDetails.add(orderDetail);
            }
        }

        map.put("nickName",nickName);
        map.put("userAddressList",addressListByUserId);
        map.put("orderDetailList",orderDetails);
        map.put("totalAmount",getCartSum(cartListByUserId));

        //生成交易码
        String tradeCode = UUID.randomUUID().toString();
        orderService.putTradeCode(tradeCode,userId);
        map.put("tradeCode",tradeCode);
        return "trade";
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
}
