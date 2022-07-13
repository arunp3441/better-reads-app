package io.arunp.betterreadsapp.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping(value="/books/{bookId}")
    private String getBook(@PathVariable String bookId, Model model){
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            String coverImage = "/images/no-images.jpg";
            if(book.getCoverIds() != null && !book.getCoverIds().isEmpty()){
                coverImage = "https://covers.openlibrary.org/b/id/" + book.getCoverIds().get(0) + "-L.jpg";
            }
            model.addAttribute("coverImage",coverImage);
            model.addAttribute("book",book);
            return "book";
        }
        return "book-not-found";
    }
}
