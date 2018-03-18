package com.casesoft.dmc.service.chart;

import com.casesoft.dmc.model.chart.StockStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casesoft.dmc.dao.task.TaskDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskCountService {
	 @Autowired
	 private TaskDao taskDao;

}
