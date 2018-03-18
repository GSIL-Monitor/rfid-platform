package com.casesoft.dmc.controller.trace;

import java.util.Comparator;

import com.casesoft.dmc.model.task.Record;

public class RecordComparator implements Comparator<Record> {

	@Override
	public int compare(Record r1, Record r2) {
		
		return r1.getToken() - r2.getToken();
	}

}
