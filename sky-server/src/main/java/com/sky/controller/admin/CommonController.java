package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Api(tags = "通用接口")
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件: {}", file.getOriginalFilename());
        
        try {
            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("上传文件不能为空");
            }
            
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                return Result.error("文件名格式不正确");
            }
            
            //获取最后结尾文件类型
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //拼上UUID
            String fileName = UUID.randomUUID().toString() + extension;
            
            //返回文件路径
            String url = aliOssUtil.upload(file.getBytes(), fileName);
            return Result.success(url);
            
        } catch (IOException e) {
            log.error("读取文件失败: {}", e.getMessage(), e);
            return Result.error("读取文件失败");
        } catch (RuntimeException e) {
            log.error("文件上传到OSS失败: {}", e.getMessage(), e);
            return Result.error("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("文件上传异常: {}", e.getMessage(), e);
            return Result.error("文件上传失败");
        }
    }
}
