package com.cjit.vms.input.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PullCuxControler implements PullDataControler {

	@Override
	public boolean decideTOPullData() throws Exception {
		String recordDateStr = Recorder.getRecoredDate();
		if(recordDateStr!=null){//有记录上次执行日期
			Date recordDate = new SimpleDateFormat(Recorder.DATE_FORMAT).parse(recordDateStr);
			CurrentPeriod currentPeriod = new CurrentPeriod(new Date());
			//当前时间点在当前循环期里，且记录的上次执行日期也在当前循环期里，则不执行，返回false
			if(currentPeriod.isInCurrentPeriod()&&currentPeriod.isInCurrentPeriod(recordDate)){
				return false;
			}
		}
		return true;
	}

}
