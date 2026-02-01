package vtc.xueqing.flower.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * Update Product DTO
 */
@Data
public class UpdateProductDTO {
    private Long merchId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long catId;
    private String description;
    private String status;
    
    // Images can be either:
    // 1. URL strings (existing images from database)
    // 2. New Blob/File uploads
    private List<String> images; // URLs or multipart parameter names
    private MultipartFile mainImage; // Optional new main image
    private List<MultipartFile> detailImages; // Optional new detail images
}
