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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/post")
@Slf4j
@Api(tags = "Post Related Interface")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Add Post
     * @param postDTO
     * @return
     */
    @ApiOperation("Add Post")
    @PostMapping
    public Result save(@RequestBody PostDTO postDTO) {

        postService.save(postDTO);
        cleanCache("rent:" + postDTO.getCategory() + ":" + postDTO.getLocation());
        return Result.success();
    }

    /**
     *
     */
    @ApiOperation("GetPostbyLocation")
    @GetMapping("/getpostbylocation")
    public Result getPosts(GetPostDTO getPostDTO) {
        String key = "rent:" + getPostDTO.getCategory() + ":" + getPostDTO.getLocation();
        List<Post> postResult = (List<Post>)redisTemplate.opsForValue().get(key);
        if(postResult != null && postResult.size() > 0) {
            return Result.success(postResult);
        }

        postResult = postService.getPosts(getPostDTO);
        redisTemplate.opsForValue().set(key, postResult);
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
        cleanCache("rent:" + postDTO.getCategory() + ":" + postDTO.getLocation());
        return Result.success();
    }

    private void cleanCache(String pattern){
        Set key = redisTemplate.keys(pattern);
        redisTemplate.delete(key);
    }

}
