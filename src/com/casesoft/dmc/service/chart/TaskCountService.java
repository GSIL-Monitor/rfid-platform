package com.casesoft.dmc.service.chart;

import com.casesoft.dmc.dao.task.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskCountService {
	 @Autowired
	 private TaskDao taskDao;

}
