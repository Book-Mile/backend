package com.bookmile.backend.domain.book.service;

import com.bookmile.backend.domain.book.dto.BooklistSearchRequestDto;
import com.bookmile.backend.domain.book.dto.BooklistSearchResponseDto;
import com.bookmile.backend.domain.book.entity.Book;
import com.bookmile.backend.domain.book.repository.BookRepository;
import com.bookmile.backend.global.utils.AladinClient;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final AladinClient aladinClient;
    private final BookRepository bookRepository;

    public BookService(AladinClient aladinClient, BookRepository bookRepository) {
        this.aladinClient = aladinClient;
        this.bookRepository = bookRepository;
    }

    // 도서 검색
    public List<BooklistSearchResponseDto> searchBooks(BooklistSearchRequestDto request) throws Exception {
        // API 호출
        String xmlResponse = aladinClient.searchBooks(request.query(), request.queryType(), request.maxResults());
        System.out.println("XML Response: " + xmlResponse); // 디버깅용 로그

        // XML 파싱
        List<BooklistSearchResponseDto> books = parseSearchResponse(xmlResponse);

        // DB 저장 (중복 확인 후)
        for (BooklistSearchResponseDto response : books) {
            System.out.println("Parsed Book: " + response); // 파싱된 도서 로그

            if (!bookRepository.existsByTitleAndAuthor(response.title(), response.author())) {
                System.out.println("Saving book: " + response.title()); // 저장 대상 로그

                Book book = new Book(
                        response.title(),
                        response.author(),
                        response.publisher(),
                        response.cover(),
                        response.link(),
                        response.description()
                );
                bookRepository.save(book);
            }
        }

        return books;
    }

    private List<BooklistSearchResponseDto> parseSearchResponse(String xml) {
        List<BooklistSearchResponseDto> books = new ArrayList<>();

        try {
            // SAX Parser 초기화
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            // 핸들러 정의
            DefaultHandler handler = new DefaultHandler() {
                private StringBuilder currentValue = new StringBuilder();
                private String title, author, publisher, cover, link, description;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    currentValue.setLength(0); // 현재 값 초기화
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    currentValue.append(ch, start, length); // 태그 값 추가
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    switch (qName) {
                        case "title" -> {
                            title = currentValue.toString().trim();
                            System.out.println("Parsed title: " + title); // title 로그
                        }
                        case "author" -> {
                            author = currentValue.toString().trim();
                            System.out.println("Parsed author: " + author); // author 로그
                        }
                        case "publisher" -> {
                            publisher = currentValue.toString().trim();
                            System.out.println("Parsed publisher: " + publisher); // publisher 로그
                        }
                        case "cover" -> {
                            cover = currentValue.toString().trim();
                            System.out.println("Parsed cover: " + cover); // cover 로그
                        }
                        case "link" -> {
                            link = currentValue.toString().trim();
                            System.out.println("Parsed link: " + link); // link 로그
                        }
                        case "description" -> {
                            description = currentValue.toString().trim();
                            System.out.println("Parsed description: " + description); // description 로그
                        }
                        case "item" -> {
                            books.add(new BooklistSearchResponseDto(title, link, author, publisher, cover, description));
                            System.out.println("Added book: " + title); // 추가된 도서 로그
                            title = author = publisher = cover = link = description = null;
                        }
                    }
                }
            };

            // XML 파싱 시작
            parser.parse(new InputSource(new StringReader(xml)), handler);

        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }

        return books;
    }
}