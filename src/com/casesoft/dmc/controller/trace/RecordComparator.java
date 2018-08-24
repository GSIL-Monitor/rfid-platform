package com.casesoft.dmc.controller.trace;

import com.casesoft.dmc.model.task.Record;

import java.util.Comparator;

public class RecordComparator implements Comparator<Record> {

	@Override
	public int compare(Record r1, Record r2) {
		
		return r1.getToken() - r2.getToken();
	}

}
