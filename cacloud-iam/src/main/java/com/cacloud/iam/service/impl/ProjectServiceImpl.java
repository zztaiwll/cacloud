package com.cacloud.iam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cacloud.iam.dao.ProjectDao;
import com.cacloud.iam.entity.ProjectEntity;
import com.cacloud.iam.service.ProjectService;
import org.springframework.stereotype.Service;


@Service("ProjectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, ProjectEntity> implements ProjectService {


}