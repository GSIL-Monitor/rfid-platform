
  CREATE OR REPLACE FORCE VIEW "CSR4_DEV"."SEARCH_BOXDTLVIEW" ("ID", "CARTONID", "COLORID", "DEVICEID", "QTY", "SIZEID", "SKU", "STYLEID", "PREQTY", "SRCTASKID", "ORIGUNITID", "ORIGID", "DESTUNITID", "DESTID", "TASKID", "TOKEN", "BILLNO", "SCANDATE", "SCANTIME") AS
  select
bd.ID as id,
bd.CARTONID as cartonId,
bd.COLORID as colorId,
bd.DEVICEID as deviceId,
bd.QTY as qty,
bd.SIZEID as sizeId,
bd.SKU as sku,
bd.STYLEID as styleId,
bd.PREQTY as preQty,

task.SRCTASKID as srctaskId,
task.origUnitId as origUnitId,
task.origId as origId,
task.destUnitId as destUnitId,
task.destId as destId,
task.id as taskId,
task.token as token,
task.billNo as billNo,
to_char(b.scanTime,'yyyy-MM-dd') as scanDate,
b.SCANTIME as scanTime

from task_boxDtl bd,task_box b,task_business task where bd.cartonId=b.cartonId and b.taskId=task.id and task.TOKEN in (8,10,23,24,25,26);

