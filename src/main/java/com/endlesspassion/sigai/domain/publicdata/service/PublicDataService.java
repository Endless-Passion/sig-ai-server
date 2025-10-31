package com.endlesspassion.sigai.domain.publicdata.service;

import com.endlesspassion.sigai.domain.publicdata.repository.PublicProfitDataRepository;
import com.endlesspassion.sigai.domain.publicdata.repository.PublicStoreDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PublicDataService {

    private final PublicProfitDataRepository publicProfitDataRepository;
    private final PublicStoreDataRepository publicStoreDataRepository;



}
