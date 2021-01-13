package com.myfactory.health.service;

import com.myfactory.health.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);
}



