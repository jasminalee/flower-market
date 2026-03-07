package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.service.MerchantService;
import vtc.xueqing.flower.service.OrderService;
import vtc.xueqing.flower.service.ProductService;
import vtc.xueqing.flower.utils.FileUploadUtils;

import javax.annotation.Resource;

/**
 * Merchant Controller
 */
@Slf4j
@Api(tags = "Merchant Management Interface")
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    @Resource
    private MerchantService merchantService;

    @Resource
    private OrderService orderService;

    @Resource
    private ProductService productService;

    @Autowired
    private FileUploadUtils fileUploadUtils;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.product-main}")
    private String productMainDir;

    @Value("${file.upload.product-detail}")
    private String productDetailDir;

    @ApiOperation("Merchant Registration")
    @PostMapping("/register")
    public Result<Merchant> register(@Validated @RequestBody Merchant merchant) {
        Merchant result = merchantService.register(merchant);
        return Result.success("Registration Successful, Please Wait for Admin Approval", result);
    }

    @ApiOperation("Merchant Login")
    @PostMapping("/login")
    public Result<Merchant> login(@Validated @RequestBody Merchant login) {
        Merchant merchant = merchantService.login(login);
        return Result.success("Login Successful", merchant);
    }

    @ApiOperation("Get Merchant Information")
    @GetMapping("/profile/{merchId}")
    public Result<Merchant> getProfile(@PathVariable Long merchId) {
        Merchant merchant = merchantService.getMerchantById(merchId);
        return Result.success(merchant);
    }
    
    @ApiOperation("Update Merchant Information")
    @PutMapping("/profile")
    public Result<Merchant> updateProfile(@RequestBody Merchant merchant) {
        try {
            Merchant updated = merchantService.updateMerchant(merchant);
            return Result.success("Update Successful", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Dashboard Data")
    @GetMapping("/dashboard")
    public Result<java.util.Map<String, Object>> getDashboardData(@RequestParam Long merchId) {
        try {
            java.util.Map<String, Object> data = merchantService.getDashboardData(merchId);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Product List")
    @GetMapping("/products")
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product>> getMerchantProducts(
            @RequestParam(required = false) Long merchId,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long catId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        try {
            // If merchId is not passed, return error message
            if (merchId == null) {
                return Result.error("Merchant ID cannot be empty");
            }
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> page;
            
            // If there are filter conditions such as name, catId, status, use detailed filtering method
            if (name != null || catId != null || status != null) {
                page = ((vtc.xueqing.flower.service.impl.MerchantServiceImpl) merchantService)
                    .getMerchantProductsWithFilter(merchId, current, size, name, catId, status);
            } else {
                // Otherwise use simple keyword search
                page = merchantService.getMerchantProducts(merchId, current, size, keyword);
            }
            
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Product Detail")
    @GetMapping("/products/{id}")
    public Result<Product> getMerchantProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return Result.error("Product does not exist");
            }
            return Result.success(product);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Order List")
    @GetMapping("/orders")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Order>> getMerchantOrders(
            @RequestParam(required = false) Long merchId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        try {
            // If merchId is not passed, return error message
            if (merchId == null) {
                return Result.error("Merchant ID cannot be empty");
            }
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Order> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
            com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Order> orderPage = 
                merchantService.getMerchantOrders(page, merchId, status, orderNo, customerName, startDate, endDate);
            
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("Get Merchant Order Details")
    @GetMapping("/orders/{id}")
    public Result<vtc.xueqing.flower.vo.MerchantOrderDetailVO> getMerchantOrderDetail(@PathVariable Long id) {
        try {
            vtc.xueqing.flower.vo.OrderDetailVO detail = orderService.getOrderDetailById(id);
            if (detail == null) {
                return Result.error("Order does not exist");
            }
            vtc.xueqing.flower.vo.MerchantOrderDetailVO vo = buildMerchantOrderDetail(detail);
            return Result.success(vo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Coupon List")
    @GetMapping("/coupons")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon>> getMerchantCoupons(
            @RequestParam(required = false) Long merchId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status
    ) {
        try {
            if (merchId == null) {
                return Result.error("Merchant ID cannot be empty");
            }
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Coupon> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
            com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon> couponPage = 
                merchantService.getMerchantCoupons(page, merchId, status);
            
            return Result.success(couponPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("Merchant Ship Order")
    @PutMapping("/orders/{id}/ship")
    public Result<Order> shipMerchantOrder(@PathVariable Long id) {
        try {
            Order order = orderService.shipOrder(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Coupon Details")
    @GetMapping("/coupons/{id}")
    public Result<vtc.xueqing.flower.entity.Coupon> getMerchantCoupon(@PathVariable Long id) {
        try {
            vtc.xueqing.flower.entity.Coupon coupon = merchantService.getMerchantCouponById(id);
            if (coupon == null) {
                return Result.error("Coupon does not exist");
            }
            return Result.success(coupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Create Merchant Coupon")
    @PostMapping("/coupons")
    public Result<vtc.xueqing.flower.entity.Coupon> createMerchantCoupon(@RequestBody vtc.xueqing.flower.entity.Coupon coupon) {
        try {
            vtc.xueqing.flower.entity.Coupon created = merchantService.createMerchantCoupon(coupon);
            return Result.success("Creation Successful", created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update Merchant Coupon")
    @PutMapping("/coupons/{id}")
    public Result<vtc.xueqing.flower.entity.Coupon> updateMerchantCoupon(
            @PathVariable Long id,
            @RequestBody vtc.xueqing.flower.entity.Coupon coupon) {
        try {
            coupon.setCouponId(id);
            vtc.xueqing.flower.entity.Coupon updated = merchantService.updateMerchantCoupon(coupon);
            return Result.success("Update Successful", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Delete Merchant Coupon")
    @DeleteMapping("/coupons/{id}")
    public Result<Void> deleteMerchantCoupon(@PathVariable Long id) {
        try {
            merchantService.deleteMerchantCoupon(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Create Product")
    @PostMapping("/products")
    public Result<Product> createProduct(
            @RequestParam("merchId") Long merchId,
            @RequestParam("name") String name,
            @RequestParam("price") java.math.BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "existingImages", required = false) String existingImages,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {
        try {
            // Create product object
            Product product = new Product();
            product.setMerchId(merchId);
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setCatId(catId);
            product.setDescription(description);
            
            // Process main image if provided
            if (mainImage != null && !mainImage.isEmpty()) {
                String mainImagePath = fileUploadUtils.uploadFile(mainImage, productMainDir);
                product.setMainImage(mainImagePath);
            }
            
            // Collect all image paths (existing URLs + newly uploaded files)
            java.util.List<String> imagePaths = new java.util.ArrayList<>();
            
            // Add existing image URLs if provided
            if (existingImages != null && !existingImages.isEmpty()) {
                try {
                    java.util.List<String> existingList = new com.fasterxml.jackson.databind.ObjectMapper()
                            .readValue(existingImages, java.util.List.class);
                    imagePaths.addAll(existingList);
                    log.info("Added {} existing images", existingList.size());
                } catch (Exception e) {
                    log.error("Failed to parse existingImages: ", e);
                }
            }
            
            // Add newly uploaded image files
            if (images != null && images.length > 0) {
                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        String imagePath = fileUploadUtils.uploadFile(image, productDetailDir);
                        imagePaths.add(imagePath);
                        log.info("Uploaded new image: {}", imagePath);
                    }
                }
            }
            
            // Set images field
            if (!imagePaths.isEmpty()) {
                product.setImages(new com.fasterxml.jackson.databind.ObjectMapper()
                        .writeValueAsString(imagePaths));
                log.info("Product images set with {} total images", imagePaths.size());
            }
            
            // Set default values
            product.setSales(0);
            product.setStatus(Constants.PRODUCT_STATUS_ACTIVE);
            product.setStockStatus(Constants.STOCK_STATUS_IN_STOCK);
            
            // Call service to create product
            Product createdProduct = merchantService.createProduct(product);
            return Result.success("Product Created Successfully", createdProduct);
        } catch (Exception e) {
            log.error("Error creating product: ", e);
            return Result.error("Failed to create product: " + e.getMessage());
        }
    }
    
    @ApiOperation("Update Product")
    @PutMapping("/products/{id}")
    public Result<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam("merchId") Long merchId,
            @RequestParam("name") String name,
            @RequestParam("price") java.math.BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "existingImages", required = false) String existingImages,
            @RequestParam(value = "images", required = false) MultipartFile[] newImages) {
        try {
            // Get existing product
            Product product = productService.getProductById(id);
            if (product == null) {
                return Result.error("Product does not exist");
            }
            
            // Update basic fields
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            if (catId != null) {
                product.setCatId(catId);
            }
            if (description != null) {
                product.setDescription(description);
            }
            if (status != null) {
                product.setStatus(status);
            }
            
            // Process main image if provided
            if (mainImage != null && !mainImage.isEmpty()) {
                String mainImagePath = fileUploadUtils.uploadFile(mainImage, productMainDir);
                product.setMainImage(mainImagePath);
            }
            
            // Combine existing and new images
            java.util.List<String> allImagePaths = new java.util.ArrayList<>();
            
            // Add existing image URLs if provided
            if (existingImages != null && !existingImages.isEmpty()) {
                try {
                    java.util.List<String> existingList = new com.fasterxml.jackson.databind.ObjectMapper()
                            .readValue(existingImages, java.util.List.class);
                    allImagePaths.addAll(existingList);
                    log.info("Added {} existing images", existingList.size());
                } catch (Exception e) {
                    log.error("Failed to parse existingImages: ", e);
                }
            }
            
            // Add new image files
            if (newImages != null && newImages.length > 0) {
                for (MultipartFile image : newImages) {
                    if (image != null && !image.isEmpty()) {
                        String imagePath = fileUploadUtils.uploadFile(image, productDetailDir);
                        allImagePaths.add(imagePath);
                        log.info("Uploaded new image: {}", imagePath);
                    }
                }
            }
            
            // Update images field
            if (!allImagePaths.isEmpty()) {
                product.setImages(new com.fasterxml.jackson.databind.ObjectMapper()
                        .writeValueAsString(allImagePaths));
                log.info("Product images updated with {} total images", allImagePaths.size());
            }
            
            // Update product
            Product updatedProduct = productService.updateProduct(product);
            return Result.success("Product Updated Successfully", updatedProduct);
        } catch (Exception e) {
            log.error("Error updating product: ", e);
            return Result.error("Failed to update product: " + e.getMessage());
        }
    }
    
    @ApiOperation("Upload Product Image")
    @PostMapping("/products/upload/image")
    public Result<String> uploadProductImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "detail") String type) {
        try {
            String uploadDir = type.equals("main") ? productMainDir : productDetailDir;
            String imagePath = fileUploadUtils.uploadFile(file, uploadDir);
            return Result.success("Image uploaded successfully", imagePath);
        } catch (Exception e) {
            log.error("Error uploading image: ", e);
            return Result.error("Failed to upload image: " + e.getMessage());
        }
    }
    
    private vtc.xueqing.flower.vo.MerchantOrderDetailVO buildMerchantOrderDetail(
            vtc.xueqing.flower.vo.OrderDetailVO detail) {
        vtc.xueqing.flower.vo.MerchantOrderDetailVO vo = new vtc.xueqing.flower.vo.MerchantOrderDetailVO();
        vo.setId(detail.getId());
        vo.setOrderNo(detail.getOrderNo());
        vo.setStatus(detail.getStatus());
        vo.setCreateTime(detail.getOrderDate());
        vo.setPayTime(detail.getPaymentTime());
        vo.setCustomerName(detail.getCustomerName());
        vo.setCustomerPhone(detail.getCustomerPhone() != null ? detail.getCustomerPhone() : detail.getReceiverPhone());
        vo.setReceiverName(detail.getReceiverName());
        vo.setReceiverPhone(detail.getReceiverPhone());
        vo.setReceiverAddress(detail.getAddress());
        vo.setItemsTotal(detail.getTotalPrice());
        vo.setDiscountAmount(detail.getDiscountAmount());
        vo.setTotalAmount(detail.getActualPrice());
        vo.setShipTime(detail.getDeliveryTime());
        vo.setCourier(null);
        vo.setTrackingNo(null);

        java.util.List<vtc.xueqing.flower.vo.MerchantOrderDetailVO.Item> items = new java.util.ArrayList<>();
        if (detail.getItems() != null) {
            for (vtc.xueqing.flower.entity.OrderItem item : detail.getItems()) {
                vtc.xueqing.flower.vo.MerchantOrderDetailVO.Item voItem = new vtc.xueqing.flower.vo.MerchantOrderDetailVO.Item();
                voItem.setProductName(item.getName());
                voItem.setProductImage(item.getMainImage());
                voItem.setPrice(item.getUnitPrice());
                voItem.setQuantity(item.getQuantity());
                items.add(voItem);
            }
        }
        vo.setItems(items);
        return vo;
    }
}
