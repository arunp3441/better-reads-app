package io.arunp.betterreadsapp.userbooks;

import io.arunp.betterreadsapp.book.Book;
import io.arunp.betterreadsapp.book.BookRepository;
import io.arunp.betterreadsapp.user.BooksByUser;
import io.arunp.betterreadsapp.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UserBooksController {

    @Autowired
    private UserBooksRepository userBooksRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    @PostMapping("/addBookForUser")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String> formData,@AuthenticationPrincipal OAuth2User principal){
        if(principal == null || principal.getAttribute("login") == null){
            return new ModelAndView("redirect:/");
        }
        String userId = principal.getAttribute("login");
        String bookId = formData.getFirst("bookId");
        if(!StringUtils.hasText(bookId)){
            return new ModelAndView("redirect:/");
        }
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();

        UserBooks userBooks = new UserBooks();
        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        key.setUserId(userId);
        key.setBookId(bookId);
        userBooks.setKey(key);
        String startDate = formData.getFirst("startDate");
        if(StringUtils.hasText(startDate)){
            userBooks.setStartDate(LocalDate.parse(startDate));
        }
        String completedDate = formData.getFirst("completedDate");
        if(StringUtils.hasText(completedDate)){
            userBooks.setCompletedDate(LocalDate.parse(completedDate));
        }
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        String rating = formData.getFirst("rating");
        if(StringUtils.hasText(rating)){
            userBooks.setRating(Integer.parseInt(rating));
        }
        userBooksRepository.save(userBooks);

        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setBookName(book.getName());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        if(StringUtils.hasText(rating)){
            booksByUser.setRating(Integer.parseInt(rating));
        }
        booksByUserRepository.save(booksByUser);

        return new ModelAndView("redirect:/books/" + bookId);
    }
}
