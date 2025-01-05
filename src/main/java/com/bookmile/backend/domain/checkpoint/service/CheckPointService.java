package com.bookmile.backend.domain.checkpoint.service;

import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;

import java.util.List;

public interface CheckPointService {
    List<CheckPoint> getPopularTemplates(Long bookId); // 인기 템플릿 조회
}