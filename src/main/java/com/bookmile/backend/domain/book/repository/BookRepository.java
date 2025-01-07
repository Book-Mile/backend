package com.bookmile.backend.domain.book.repository;

import com.bookmile.backend.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIsbn13(String isbn13); // ISBN13으로 책 조회
}