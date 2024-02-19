package com.gb.api;

import com.gb.BookProvider;
import com.github.javafaker.Faker;
import com.gb.timer.Timer;
import org.slf4j.event.Level;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZoneId;
import java.util.*;

@RestController
@Timer(level = Level.WARN)
@RequestMapping("/api/issue")
public class IssueController {

  private final Faker faker;
  private final BookProvider bookProvider;
  private final List<Issue> issues;

  public IssueController(BookProvider bookProvider) {
    this.faker = new Faker();
    this.bookProvider = bookProvider;
    this.issues = new ArrayList<>();

    refreshData();
  }

  @GetMapping
  public List<Issue> getAll() {
    return issues;
  }
  @Timer(level = Level.WARN)
  @GetMapping("/refresh")
  public List<Issue> refresh() {
    refreshData();
    return issues;
  }

  private void refreshData() {
    issues.clear();
    for (int i = 0; i < 15; i++) {
      Issue issue = new Issue();
      issue.setId(UUID.randomUUID());

      Date between = faker.date().between(startOfYear(), endOfYear());
      issue.setIssuedAt(between.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
      issue.setBookId(bookProvider.getRandomBookId());
      issue.setReaderId(UUID.randomUUID());

      issues.add(issue);
    }
  }

  private Date startOfYear() {
    Calendar instance = Calendar.getInstance();
    instance.set(Calendar.YEAR, 2024);
    instance.set(Calendar.MONTH, Calendar.JANUARY);
    instance.set(Calendar.DAY_OF_MONTH, 1);
    return instance.getTime();
  }

  private Date endOfYear() {
    Calendar instance = Calendar.getInstance();
    instance.set(Calendar.YEAR, 2024);
    instance.set(Calendar.MONTH, Calendar.DECEMBER);
    instance.set(Calendar.DAY_OF_MONTH, 31);
    return instance.getTime();
  }
}
