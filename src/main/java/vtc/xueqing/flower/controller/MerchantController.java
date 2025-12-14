package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.service.MerchantService;

import javax.annotation.Resource;

/**
 * 商家Controller
 */
@Slf4j
@Api(tags = "商家管理接口")
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    @Resource
    private MerchantService merchantService;

    @ApiOperation("商家注册")
    @PostMapping("/register")
    public Result<Merchant> register(@Validated @RequestBody Merchant merchant) {
        Merchant result = merchantService.register(merchant);
        return Result.success("注册成功，请等待管理员审核", result);
    }

    @ApiOperation("商家登录")
    @PostMapping("/login")
    public Result<Merchant> login(@Validated @RequestBody Merchant login) {
        Merchant merchant = merchantService.login(login);
        return Result.success("登录成功", merchant);
    }

    @ApiOperation("获取商家信息")
    @GetMapping("/profile/{merchId}")
    public Result<Merchant> getProfile(@PathVariable Long merchId) {
        Merchant merchant = merchantService.getMerchantById(merchId);
        return Result.success(merchant);
    }
    
    @ApiOperation("更新商家信息")
    @PutMapping("/profile")
    public Result<Merchant> updateProfile(@RequestBody Merchant merchant) {
        try {
            Merchant updated = merchantService.updateMerchant(merchant);
            return Result.success("更新成功", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家工作台数据")
    @GetMapping("/dashboard")
    public Result<java.util.Map<String, Object>> getDashboardData(@RequestParam Long merchId) {
        try {
            java.util.Map<String, Object> data = merchantService.getDashboardData(merchId);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
