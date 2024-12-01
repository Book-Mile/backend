package com.bookmile.backend.domain.book.repository;

import com.bookmile.backend.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);
}
