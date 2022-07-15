package io.arunp.betterreadsapp.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder().codecs(configure ->
                        configure.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build()).baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping(value="/search")
    public String getSearchResults(@RequestParam String query, Model model){
        Mono<SearchResult> searchResultMono = this.webClient.get().uri("?q={query}",query).retrieve().bodyToMono(SearchResult.class);
        SearchResult searchResult = searchResultMono.block();
        if(searchResult != null) {
            List<SearchResultBook> books = searchResult.getSearchResultBooks().stream().limit(12).peek(book -> {
                book.setKey(book.getKey().replace("/works/",""));
                String coverId = book.getCoverId();
                if(StringUtils.hasText(coverId)){
                    coverId = "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg";
                }
                else{
                    coverId = "/images/no-images.jpg";
                }
                book.setCoverId(coverId);
            }).toList();
            model.addAttribute("books", books);
        }
        return "search";
    }
}
