package com.project.springbootproject;

import com.project.springbootproject.model.Book;
import com.project.springbootproject.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootProjectApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Test");
            book.setAuthor("Serhii");
            book.setIsbn("123456");
            book.setPrice(BigDecimal.valueOf(500));
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}

