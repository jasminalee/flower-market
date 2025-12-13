package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.service.OrderService;

import javax.annotation.Resource;

/**
 * 订单控制器
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Resource
    private OrderService orderService;
    
    @ApiOperation("创建订单")
    @PostMapping
    public Result<Order> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return Result.success(createdOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取订单列表（分页）")
    @GetMapping
    public Result<IPage<Order>> getOrderList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId,
            @ApiParam("商家ID") @RequestParam(required = false) Long merchId,
            @ApiParam("订单状态") @RequestParam(required = false) String status
    ) {
        try {
            Page<Order> page = new Page<>(current, size);
            IPage<Order> orderPage = orderService.getOrderPage(page, userId, merchId, status);
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable("id") Long id) {
        try {
            Order order = orderService.getOrderById(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("支付订单")
    @PutMapping("/{id}/pay")
    public Result<Order> payOrder(
            @PathVariable("id") Long id,
            @ApiParam("支付方式：ALIPAY-支付宝，WECHAT-微信，BALANCE-余额") @RequestParam String paymentMethod
    ) {
        try {
            Order order = orderService.payOrder(id, paymentMethod);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("取消订单")
    @PutMapping("/{id}/cancel")
    public Result<Order> cancelOrder(
            @PathVariable("id") Long id,
            @ApiParam("取消原因") @RequestParam(required = false) String cancelReason
    ) {
        try {
            Order order = orderService.cancelOrder(id, cancelReason);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("确认收货")
    @PutMapping("/{id}/confirm")
    public Result<Order> confirmOrder(@PathVariable("id") Long id) {
        try {
            Order order = orderService.confirmOrder(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("商家发货")
    @PutMapping("/{id}/ship")
    public Result<Order> shipOrder(@PathVariable("id") Long id) {
        try {
            Order order = orderService.shipOrder(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
