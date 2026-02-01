package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.dto.OrderCreateRequest;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.service.OrderService;
import vtc.xueqing.flower.vo.OrderDetailVO;
import vtc.xueqing.flower.vo.OrderVO;
import vtc.xueqing.flower.vo.ParentOrderCreateResult;

import javax.annotation.Resource;

/**
 * Order Controller
 */
@Api(tags = "Order Management")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Resource
    private OrderService orderService;
    
    @ApiOperation("Create Order")
    @PostMapping
    public Result<ParentOrderCreateResult> createOrder(@RequestBody OrderCreateRequest request) {
        try {
            ParentOrderCreateResult createdOrder = orderService.createOrder(request);
            return Result.success(createdOrder);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Order List (Pagination)")
    @GetMapping
    public Result<IPage<OrderVO>> getOrderList(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("User ID") @RequestParam(required = false) Long userId,
            @ApiParam("Merchant ID") @RequestParam(required = false) Long merchId,
            @ApiParam("Order Status") @RequestParam(required = false) String status
    ) {
        try {
            Page<Order> page = new Page<>(current, size);
            IPage<OrderVO> orderPage = orderService.getOrderPage(page, userId, merchId, status);
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Order Details")
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getOrderById(@PathVariable("id") Long id) {
        try {
            OrderDetailVO orderDetail = orderService.getOrderDetailById(id);
            return Result.success(orderDetail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Pay Order")
    @PutMapping("/{id}/pay")
    public Result<Order> payOrder(
            @PathVariable("id") Long id,
            @ApiParam("Payment Method: ALIPAY-Alipay, WECHAT-WeChat, BALANCE-Balance") @RequestParam String paymentMethod
    ) {
        try {
            Order order = orderService.payOrder(id, paymentMethod);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Cancel Order")
    @PutMapping("/{id}/cancel")
    public Result<Order> cancelOrder(
            @PathVariable("id") Long id,
            @ApiParam("Cancellation Reason") @RequestParam(required = false) String cancelReason
    ) {
        try {
            Order order = orderService.cancelOrder(id, cancelReason);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Confirm Receipt")
    @PutMapping("/{id}/confirm")
    public Result<Order> confirmOrder(@PathVariable("id") Long id) {
        try {
            Order order = orderService.confirmOrder(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Merchant Ship Order")
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
