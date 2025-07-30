package spring.cloud.service;

import spring.cloud.blog.api.pojo.AddBlogInfoRequest;
import spring.cloud.blog.api.pojo.BlogInfoResponse;
import spring.cloud.blog.api.pojo.UpBlogRequest;

import java.util.List;

public interface BlogService {
    List<BlogInfoResponse> getList();

    BlogInfoResponse getBlogDeatil(Integer blogId);

    Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest);

    Boolean update(UpBlogRequest upBlogRequest);

    Boolean delete(Integer blogId);
}