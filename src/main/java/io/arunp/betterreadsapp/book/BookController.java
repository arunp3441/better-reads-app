package io.arunp.betterreadsapp.book;

import io.arunp.betterreadsapp.userbooks.UserBooks;
import io.arunp.betterreadsapp.userbooks.UserBooksPrimaryKey;
import io.arunp.betterreadsapp.userbooks.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBooksRepository userBooksRepository;

    @GetMapping(value="/books/{bookId}")
    private String getBook(@PathVariable String bookId, Model model,@AuthenticationPrincipal OAuth2User principal){
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            String coverImage = "/images/no-images.jpg";
            if(book.getCoverIds() != null && !book.getCoverIds().isEmpty()){
                coverImage = "https://covers.openlibrary.org/b/id/" + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImage",coverImage);
            model.addAttribute("book",book);
            if(principal != null && principal.getAttribute("login")!= null){
                String loginId = principal.getAttribute("login");
                model.addAttribute("loginId" , loginId);
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setUserId(loginId);
                key.setBookId(bookId);
                Optional<UserBooks> userBooks = userBooksRepository.findById(key);
                if(userBooks.isPresent()){
                    model.addAttribute("userBooks" , userBooks.get());
                }
                else{
                    model.addAttribute("userBooks" , new UserBooks());
                }
            }
            return "book";
        }
        return "book-not-found";
    }
}
