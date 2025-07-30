package spring.cloud.blog.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.cloud.blog.api.pojo.AddBlogInfoRequest;
import spring.cloud.blog.api.pojo.BlogInfoResponse;
import spring.cloud.blog.api.pojo.UpBlogRequest;
import spring.cloud.common.pojo.Result;

import java.util.List;

@FeignClient(value = "blog-service", path = "/blog")
public interface BlogServiceApi {
    @RequestMapping("/getList")
    Result<List<BlogInfoResponse>> getList();

    @RequestMapping("/getBlogDetail")
    Result<BlogInfoResponse> getBlogDetail(@RequestParam("blogId") Integer blogId);

    @RequestMapping("/add")
    Result<Boolean> addBlog(@RequestBody AddBlogInfoRequest addBlogInfoRequest);

    @RequestMapping("/update")
    Result<Boolean> updateBlog(@RequestBody UpBlogRequest upBlogRequest);

    @RequestMapping("/delete")
    Result<Boolean> deleteBlog(@RequestParam("blogId") Integer blogId);
}