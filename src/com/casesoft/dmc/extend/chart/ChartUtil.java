package com.casesoft.dmc.extend.chart;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.extend.chart.ChartVo.Serie;
import com.casesoft.dmc.model.task.Business;
import com.casesoft.dmc.model.task.BusinessDtl;

import java.util.*;


public class ChartUtil {
	public static ChartVo convertToChartVo(List<Business> voList) throws Exception {
	    ChartVo<Integer, String, long[]> chartVo = new ChartVo<Integer, String, long[]>();
	    for (Business vo : voList) {
	      Date d =vo.getBeginTime();
	      long time = d.getTime();
	      String name = CacheManager.getUnitById(vo.getOwnerId()).getName();
	      boolean isHave = false;
	      for (Serie s : chartVo.getSeries()) {
	        if (s.getName().equals(name)) {
	          long[] dataArr = new long[2];
	          dataArr[0] = time;
	          dataArr[1] = vo.getTotEpc();
	          s.addData(dataArr);
	          isHave = true;
	          break;
	        }
	      }
	      if (!isHave) {
	        ChartVo<Integer, String, long[]>.Serie serie = new ChartVo<Integer, String, long[]>().new Serie();
	        serie.setName(name);
	        long[] dataArr = new long[2];
	        dataArr[0] = time;
	        dataArr[1] = vo.getTotEpc();
	        serie.addData(dataArr);
	        chartVo.addSerie(serie);
	      }
	    }

	    for (Serie s : chartVo.getSeries()) {
	      ChartVo<Integer, String, long[]>.DataComparator comparator = new ChartVo<Integer, String, long[]>().new DataComparator();
	      Collections.sort(s.getData(), comparator);
	    }
	    return chartVo;
	  }
	  public static String convertToChartResult(List<BusinessDtl> listScore, List<BusinessDtl> saleObjs) {
		    StringBuffer categories = new StringBuffer();
		    StringBuffer data = new StringBuffer();
		    StringBuffer data2 = new StringBuffer();
		    String name = "试衣量";
		    String name2 = "销售量";
		    for (BusinessDtl o : listScore) {
		      categories.append(",\"").append(o.getSku()).append("\"");
		      data.append("," + o.getQty());
		      boolean have = false;
		      for (BusinessDtl dtl : saleObjs) {
		        if (dtl.getSku().equals(o.getSku())) {
		          have = true;
		          data2.append(",").append(dtl.getQty());
		          break;
		        }
		      }
		      if (!have) {
		        data2.append(",").append(0);
		      }
		    }
		    String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
		        + "\"name\":\"" + name2 + "\",\"data\":[" + data2.substring(1) + "]},{" + "\"name\":\""
		        + name + "\",\"data\":[" + data.substring(1) + "]" + "}] }";
		    // String result = "{\"categories\":[" + categories.substring(1) + "]," + "\"series\":[{"
		    // + "\"name\":\"" + name + "\",\"data\":[" + data.substring(1) + "]} ] }";
		    return result;
		  }

	public static Map<String,Object> countMonthTaskQty(String tokens, List<Object[]> dataList) {
		String[] strTokenArray = tokens.split(",");

 		Map<String,Object> resultMap = new HashMap<String,Object>();
		String xAxis = "";
		for(Object[] os : dataList) { // totStyle,totSku,totEpc,year,month,day,token
			String year = os[3].toString();
			String month = os[4].toString();
			//String token = os[5].toString();
			if(!xAxis.contains(year+"-"+month)) {
				xAxis += ("," + year + "-" + month);
			}
		}
		String[] xAxisData = xAxis.substring(1).split(",");
		Arrays.sort(xAxisData, new Comparator<String>() {//按日期排序
			@Override
			public int compare(String o1, String o2) {
				String[] temp1 = o1.split("-");
				String[] temp2 = o2.split("-");
				return temp1[0].equals(temp2[0])?Integer.parseInt(temp1[1])-Integer.parseInt(temp2[1]):
						Integer.parseInt(temp1[0])-Integer.parseInt(temp2[0]);
			}
		});

		int[][] seriesData = new int[strTokenArray.length][xAxisData.length];
		for(String xAxisDate : xAxisData) {
			for (Object[] os : dataList) { // totStyle,totSku,totEpc,year,month,day,token
				String year = os[3].toString();
				String month = os[4].toString();
				String token = os[5].toString();

				String date = year +"-"+month;

				int yIndex = overrideArrays_binarySearch(strTokenArray, token);
                int xIndex = overrideArrays_binarySearch(xAxisData,date);

				seriesData[yIndex][xIndex] = Integer.parseInt(os[2].toString());
			}
		}

		resultMap.put("xAxis",xAxisData);
		resultMap.put("seriesData",seriesData);
		return resultMap;
	}

	public static Map<String,Object> countDayTaskQty(String tokens, List<Object[]> dataList) {
		String[] strTokenArray = tokens.split(",");

		Map<String,Object> resultMap = new HashMap<String,Object>();
		String xAxis = "";
		for(Object[] os : dataList) { // totStyle,totSku,totEpc,year,month,day,token
			String month = os[3].toString();
			String day = os[4].toString();
			//String token = os[5].toString();
			if(!xAxis.contains(month+"-"+day)) {
				xAxis += ("," + month + "-" + day);
			}
		}
		String[] xAxisData = xAxis.substring(1).split(",");
		Arrays.sort(xAxisData, new Comparator<String>() {//按日期排序
			@Override
			public int compare(String o1, String o2) {
				String[] temp1 = o1.split("-");
				String[] temp2 = o2.split("-");
				return temp1[0].equals(temp2[0])?Integer.parseInt(temp1[1])-Integer.parseInt(temp2[1]):
						Integer.parseInt(temp1[0])-Integer.parseInt(temp2[0]);
			}
		});

		int[][] seriesData = new int[strTokenArray.length][xAxisData.length];
		for(String xAxisDate : xAxisData) {
			for (Object[] os : dataList) { // totStyle,totSku,totEpc,year,month,day,token
				String month = os[3].toString();
				String day = os[4].toString();
				String token = os[5].toString();

				String date = month +"-"+day;

				int yIndex = overrideArrays_binarySearch(strTokenArray, token);
				int xIndex = overrideArrays_binarySearch(xAxisData, date);

				seriesData[yIndex][xIndex] = Integer.parseInt(os[2].toString());
			}
		}

		resultMap.put("xAxis",xAxisData);
		resultMap.put("seriesData",seriesData);
		return resultMap;
	}

	private static int overrideArrays_binarySearch(String[] array, String key) {
		int index = -1;
		for(int i=0;i<array.length;i++) {
			if(array[i].equals(key)) {
				index = i;
				break;
			}
		}
		return index;
	}
}
