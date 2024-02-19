package com.gb.api;

import com.gb.timer.Timer;
import com.github.javafaker.Faker;
import org.slf4j.event.Level;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.api.Author;
import ru.gb.api.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Timer(level = Level.WARN)
@RequestMapping("/api/book")
public class BookController {

  private final Faker faker;
  private final List<Book> books;

  public BookController() {
    this.faker = new Faker();
    final List<Book> books = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      Book book = new Book();
      book.setId(UUID.randomUUID());
      book.setName(faker.book().title());

      Author author = new Author();
      author.setId(UUID.randomUUID());
      author.setFirstName(faker.name().firstName());
      author.setLastName(faker.name().lastName());
      book.setAuthor(author);

      books.add(book);
    }

    this.books = List.copyOf(books);
  }
  @Timer(level = Level.WARN)
  @GetMapping
  public List<Book> getAll() {
    return books;
  }

  @Timer(level = Level.WARN)
  @GetMapping("/random")
  public Book getRandom() {
    final int randomIndex = faker.number().numberBetween(0, books.size());
    return books.get(randomIndex);
  }

}
