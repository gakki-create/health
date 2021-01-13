package com.myfactory.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myfactory.health.dao.MemberDao;
import com.myfactory.health.pojo.Member;
import com.myfactory.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Gakki
 * @version 1.0
 * @description: TODO
 * @date 2021/1/13 17:19
 */

@Service(interfaceClass =MemberService.class )
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }
}
