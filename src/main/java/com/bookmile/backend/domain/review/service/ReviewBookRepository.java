package com.bookmile.backend.domain.review.service;

import com.bookmile.backend.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewBookRepository extends JpaRepository<Book, Long> {
}
