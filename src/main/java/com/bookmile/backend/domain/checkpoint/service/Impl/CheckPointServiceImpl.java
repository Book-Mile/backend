package com.bookmile.backend.domain.checkpoint.service.Impl;

import com.bookmile.backend.domain.checkpoint.entity.CheckPoint;
import com.bookmile.backend.domain.checkpoint.repository.CheckPointRepository;
import com.bookmile.backend.domain.checkpoint.service.CheckPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckPointServiceImpl implements CheckPointService {

    private final CheckPointRepository checkPointRepository;

    @Override
    public List<CheckPoint> getPopularTemplates(Long bookId) {
        return checkPointRepository.findPopularTemplatesByBookId(bookId);
    }
}