package spring.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.cloud.blog.api.pojo.AddBlogInfoRequest;
import spring.cloud.blog.api.pojo.BlogInfoResponse;
import spring.cloud.blog.api.pojo.UpBlogRequest;
import spring.cloud.dataobject.BlogInfo;
import spring.cloud.mapper.BlogMapper;
import spring.cloud.service.BlogService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<BlogInfoResponse> getList() {
        //从数据库查询数据
        List<BlogInfo> blogInfos = blogMapper.selectList(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getDeleteFlag, 0).orderByDesc(BlogInfo::getId));
        //转换数据格式
        List<BlogInfoResponse> blogInfoResponses = blogInfos.stream().map(blogInfo -> {
            BlogInfoResponse response = new BlogInfoResponse();
            BeanUtils.copyProperties(blogInfo, response);
            return response;
        }).collect(Collectors.toList());
        return blogInfoResponses;
    }

    @Override
    public BlogInfoResponse getBlogDeatil(Integer blogId) {
        BlogInfoResponse blogInfoResponse = new BlogInfoResponse();
        BlogInfo blogInfo = getBlogInfo(blogId);
        BeanUtils.copyProperties(blogInfo, blogInfoResponse);

        return blogInfoResponse;
    }

    @Override
    public Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest) {

        BlogInfo blogInfo = new BlogInfo();
        BeanUtils.copyProperties(addBlogInfoRequest, blogInfo);
        Integer result = null;
        try {
            result = insertBlog(blogInfo);
            if (result==1){
                return true;
            }
        }catch (Exception e){
            log.error("发布博客失败, e:", e);
        }
        return false;
    }

    @Override
    public Boolean update(UpBlogRequest upBlogRequest) {
        BlogInfo blogInfo = new BlogInfo();
        BeanUtils.copyProperties(upBlogRequest, blogInfo);

        Integer result =null;
        try {
            result = this.updateBlog(blogInfo);
            if (result>=1){
                return true;
            }

        }catch (Exception e){
            log.error("updateBlog 发生异常, e: ", e);
        }
        return false;
    }

    @Override
    public Boolean delete(Integer blogId) {
        BlogInfo blogInfo = new BlogInfo();
        blogInfo.setId(blogId);
        blogInfo.setDeleteFlag(1);
        Integer result =null;
        try {
            result = this.updateBlog(blogInfo);
            if (result>=1){
                return true;
            }

        }catch (Exception e){
            log.error("updateBlog 发生异常, e: ", e);
        }
        return false;
    }

    private BlogInfo getBlogInfo(Integer blogId) {
        return blogMapper.selectOne(new LambdaQueryWrapper<BlogInfo>()
                .eq(BlogInfo::getDeleteFlag, 0).eq(BlogInfo::getId, blogId));
    }
    private Integer insertBlog(BlogInfo blogInfo) {
        return blogMapper.insert(blogInfo);
    }

    public Integer updateBlog(BlogInfo blogInfo) {
        return blogMapper.updateById(blogInfo);
    }
}