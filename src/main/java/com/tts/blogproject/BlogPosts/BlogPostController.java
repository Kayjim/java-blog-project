package com.tts.blogproject.BlogPosts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogPostController {


    private BlogPostRepository blogPostRepository;
    private List<BlogPost> posts;

    public BlogPostController(ArrayList<BlogPost> posts, BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
        this.posts = posts;
    }

    //Route to the root page
    @GetMapping(value="/")
    public String index(BlogPost blogPost, Model model) {
        model.addAttribute("posts", posts);
        return "index";    }

    //create a new post
        @GetMapping(value = "/blog_posts/new")
        public String newBlog (BlogPost blogPost) {
        return "new";
        }

    private void mirrorDB() {
        Iterable<BlogPost> blogList = blogPostRepository.findAll();
        posts.clear();
        for(BlogPost blog: blogList) {
            posts.add(blog);
        }
    }

    @GetMapping("/blog_posts/{id}/edit")
    public String editBlogEntryView(@PathVariable("id") Long id, Model model) {
        Optional<BlogPost> op = blogPostRepository.findById(id);
        model.addAttribute("blogPost",op.get());
        System.out.println(op.get().toString());
        return "edit";
    }

    @PutMapping("/blog_posts/{id}/edit")
    public String editBlogEntryPut(BlogPost bp, Model model) {
        System.out.println(bp.toString());
        blogPostRepository.save(bp);
        mirrorDB();
        model.addAttribute("title",bp.getTitle());
        model.addAttribute("author",bp.getAuthor());
        Model blogEntry = model.addAttribute("blogEntry", bp.getBlogEntry());
        return "result";
    }

    //Deletes a blog post
    @DeleteMapping("/blog_posts/{id}/delete")
    public String deleteBlogPost(@PathVariable("id") Long id) {
        blogPostRepository.deleteById(id);
        mirrorDB();
        return "result";
    }

    //	private BlogPost blogPost;
    @PostMapping(value = "/blog_posts/new")
    public String addNewBlogPost(BlogPost blogPost, Model model) {
        // Shortcut (remains to be seen if works):
        blogPostRepository.save(blogPost);
        posts.add(blogPost);
        //Longer version:
        //blogPostRepository.save(new BlogPost(blogPost.getTitle(), blogPost.getAuthor(), blogPost.getBlogEntry()));
        model.addAttribute("title", blogPost.getTitle());
        model.addAttribute("author", blogPost.getAuthor());
        model.addAttribute("blogEntry", blogPost.getBlogEntry());
        return "result";
    }
}
