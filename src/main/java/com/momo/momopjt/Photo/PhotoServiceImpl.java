package com.momo.momopjt.Photo;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    @Override
    public void savePhoto(Photo photo) {

    }
}
