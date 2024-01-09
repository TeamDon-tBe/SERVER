package com.dontbe.www.DontBeServer.api.ghost.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GhostQueryService {

    // 조회하는 API만
}
