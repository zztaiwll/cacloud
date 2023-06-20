package com.cacloud.iam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cacloud.iam.dao.AccountDao;
import com.cacloud.iam.entity.AccountEntity;
import com.cacloud.iam.service.AccountService;
import org.springframework.stereotype.Service;


@Service("AccountService")
public class AccountServiceImpl extends ServiceImpl<AccountDao, AccountEntity> implements AccountService {


}