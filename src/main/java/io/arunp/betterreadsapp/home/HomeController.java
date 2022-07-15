package io.arunp.betterreadsapp.home;


import io.arunp.betterreadsapp.user.BooksByUser;
import io.arunp.betterreadsapp.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal){
        if(principal == null || principal.getAttribute("login") == null){
            return "index";
        }
        String loginId = principal.getAttribute("login");
        Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(loginId, CassandraPageRequest.of(0,100));
        List<BooksByUser> booksByUser = booksSlice.getContent();
        booksByUser = booksByUser.stream().distinct().peek(book -> {
            String coverImageUrl = "/images/no-images.jpg";
            if(book.getCoverIds() != null && !book.getCoverIds().isEmpty()){
                coverImageUrl = "https://covers.openlibrary.org/b/id/" + book.getCoverIds().get(0) + "-M.jpg";
            }
            book.setCoverUrl(coverImageUrl);
        }).collect(Collectors.toList());
        model.addAttribute("books",booksByUser);
        return "home";
    }
}

