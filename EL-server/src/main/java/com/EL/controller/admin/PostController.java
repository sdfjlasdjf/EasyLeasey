package com.EL.controller.admin;


import com.EL.context.BaseContext;
import com.EL.dto.GetPostDTO;
import com.EL.dto.PostDTO;
import com.EL.dto.PostPageQueryDTO;
import com.EL.entity.Post;
import com.EL.result.PageResult;
import com.EL.result.Result;
import com.EL.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/post")
@Slf4j
@Api(tags = "Post Related Interface")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     * Add Post
     * @param postDTO
     * @return
     */
    @ApiOperation("Add Post")
    @PostMapping
    public Result save(@RequestBody PostDTO postDTO) {
        postService.save(postDTO);
        return Result.success();
    }

    /**
     *
     */
    @ApiOperation("GetPostbyLocation")
    @GetMapping("/getpostbylocation")
    public Result getPosts(GetPostDTO getPostDTO) {
        List<Post> postResult = postService.getPosts(getPostDTO);
        return Result.success(postResult);

    }

    @GetMapping("/page")
    @ApiOperation("Query Post by Page")
    public Result<PageResult> page(@RequestBody PostPageQueryDTO postPageQueryDTO) {
        PageResult pageResult = postService.pageQuery(postPageQueryDTO);
        return Result.success(pageResult);
    }
    // Get Posts Liked by User
    @ApiOperation("Get Liked Posts")
    @GetMapping("/liked")
    public Result<List<Post>> getLikedPosts() {

        List<Post> likedPosts = postService.getLikedPosts(BaseContext.getCurrentId());
        return Result.success(likedPosts);
    }

    // Get Posts Created by User
    @ApiOperation("Get User's Posts")
    @GetMapping("/userposts")
    public Result<List<Post>> getUserPosts() {
        List<Post> userPosts = postService.getUserPosts(BaseContext.getCurrentId());
        return Result.success(userPosts);
    }

    @ApiOperation("Get post detail")
    @GetMapping("/postdetail")
    public Result<Post> getPostDetail(Long postId){
        Post postDetail = postService.getPostDetail(postId);
        return Result.success(postDetail);
    }

    @ApiOperation("Update post")
    @PutMapping("/update")
    public Result update(@RequestBody PostDTO postDTO) {
        postService.update(postDTO);
        return Result.success();
    }

}
