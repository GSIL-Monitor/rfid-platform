package com.casesoft.dmc.model.task;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Created by WingLi on 2014/11/23.
 */
@Entity
@Table(name = "TASK_CLOUDRECORD")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CloudRecord extends Record{
}
