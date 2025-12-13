package vtc.xueqing.flower.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.exception.BusinessException;
import vtc.xueqing.flower.mapper.MerchantMapper;
import vtc.xueqing.flower.service.MerchantService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 商家服务实现类
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    @Resource
    private MerchantMapper merchantMapper;

    @Override
    public Merchant register(Merchant merchant) {
        // 1. 检查邮箱是否已存在
        LambdaQueryWrapper<Merchant> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(Merchant::getEmail, merchant.getEmail());
        if (merchantMapper.selectCount(emailWrapper) > 0) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 2. 检查手机号是否已存在
        LambdaQueryWrapper<Merchant> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(Merchant::getPhone, merchant.getPhone());
        if (merchantMapper.selectCount(phoneWrapper) > 0) {
            throw new BusinessException("该手机号已被注册");
        }

        // 3. 使用MD5加密密码
        merchant.setPassword(SecureUtil.md5(merchant.getPassword()));
        // 商家注册后默认为待审核状态
        merchant.setStatus(Constants.MERCHANT_STATUS_PENDING);
        merchant.setCreateDate(LocalDateTime.now());
        merchant.setUpdateDate(LocalDateTime.now());

        // 4. 保存到数据库
        int result = merchantMapper.insert(merchant);
        if (result == 0) {
            throw new BusinessException("注册失败");
        }

        log.info("商家注册成功，邮箱：{}, 状态：待审核", merchant.getEmail());

        // 5. 返回密码置空的merchant对象
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant login(Merchant login) {
        // 1. 根据邮箱或手机号查询商家
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Merchant::getEmail, login.getEmail())
                .or()
                .eq(Merchant::getPhone, login.getEmail()));

        Merchant merchant = merchantMapper.selectOne(wrapper);
        if (merchant == null) {
            throw new BusinessException("账号不存在");
        }

        // 2. 验证密码（MD5加密后比较）
        String encryptedPassword = SecureUtil.md5(login.getPassword());
        if (!encryptedPassword.equals(merchant.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 检查商家状态
        if (Constants.MERCHANT_STATUS_PENDING.equals(merchant.getStatus())) {
            throw new BusinessException("您的商家账号正在审核中，请耐心等待");
        }
        if (Constants.MERCHANT_STATUS_REJECTED.equals(merchant.getStatus())) {
            throw new BusinessException("您的商家账号审核未通过");
        }
        if (Constants.MERCHANT_STATUS_SUSPENDED.equals(merchant.getStatus())) {
            throw new BusinessException("您的商家账号已被暂停");
        }

        log.info("商家登录成功，ID：{}, 邮箱：{}", merchant.getMerchId(), merchant.getEmail());

        // 4. 返回密码置空的merchant对象
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant getMerchantById(Long merchId) {
        Merchant merchant = merchantMapper.selectById(merchId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }
        // 密码置空
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public boolean updateMerchant(Merchant merchant) {
        merchant.setUpdateDate(LocalDateTime.now());
        int result = merchantMapper.updateById(merchant);
        return result > 0;
    }
}
