package com.cjit.vms.interval.function;

import java.util.List;
import java.util.Map;

/**
 * @作者： 刘俊杰
 * @日期： 2018-08-09
 * @描述： 每当更改跑批任务时调用此类，更改定时器执行时间
 */
public class IntervalChange {
	public void intervalChange(Map map) throws Exception {
		
		String cname=map.get("cname").toString();
		int hour = Integer.parseInt(map.get("hour").toString());
		int minute = Integer.parseInt(map.get("minute").toString());
		int second = Integer.parseInt(map.get("second").toString());
		long intervalHour=Long.parseLong(map.get("intervalHour").toString()) * 60 * 60 *1000;
		long intervalMinute=Long.parseLong(map.get("intervalMinute").toString()) * 60 * 1000;
		long intervalSecond=Long.parseLong(map.get("intervalSecond").toString()) * 1000;
		
		long sendInterval = intervalHour + intervalMinute + intervalSecond;
		
		System.out.println("intervalChange:" + sendInterval);
		
		IntervalUtil intervalUtil = new IntervalUtil();
		
		List<Interval> intervalList = intervalUtil.getIntervalList();
		int i = 0;
		for(Interval list : intervalList) {
			if(cname.equals(list.getTimerId())) {
				intervalUtil.destory(list.getTimer());
				Interval interval = new Interval();
				interval.setSendInterval(sendInterval);
				interval.setHour(hour);
				interval.setMinute(minute);
				interval.setSecond(second);
				interval.setTimerId(cname);
				intervalList.set(i, interval);
			}
			i++;
		}
		
		intervalUtil.setInterval(cname);
		
	}
}
