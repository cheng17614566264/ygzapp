package com.cjit.vms.electronics.model;

public class ElectroniscStatusUtil {
	/**
	 * 电子发票初始页
	 */
	public static final String ELECTRONICS_TRANS_STATUS_201 = "201";
	public static final String ELECTRONICS_TRANS_STATUS_201_CH = "自动开具失败";
	public static final String ELECTRONICS_TRANS_STATUS_202 = "202";
	public static final String ELECTRONICS_TRANS_STATUS_202_CH = "手动开具失败";
	public static final String ELECTRONICS_TRANS_STATUS_203 = "203";
	public static final String ELECTRONICS_TRANS_STATUS_203_CH = "保全增人";
	public static final String ELECTRONICS_TRANS_STATUS_204 = "204";
	public static final String ELECTRONICS_TRANS_STATUS_204_CH = "保全减人";
	public static final String ELECTRONICS_TRANS_STATUS_205 = "205";
	public static final String ELECTRONICS_TRANS_STATUS_205_CH = "保全增费";
	public static final String ELECTRONICS_TRANS_STATUS_206 = "206";
	public static final String ELECTRONICS_TRANS_STATUS_206_CH = "保全减费";

	/**
	 * 电子发票红冲页
	 */
	public static final String ELECTRONICS_REDBILL_STATUS_301 = "301";
	public static final String ELECTRONICS_REDBILL_STATUS_301_CH = "已开具蓝票";
	public static final String ELECTRONICS_REDBILL_STATUS_302 = "302";
	public static final String ELECTRONICS_REDBILL_STATUS_302_CH = "未开具红票";
	public static final String ELECTRONICS_REDBILL_STATUS_303 = "303";
	public static final String ELECTRONICS_REDBILL_STATUS_303_CH = "审核中";
	public static final String ELECTRONICS_REDBILL_STATUS_304 = "304";
	public static final String ELECTRONICS_REDBILL_STATUS_304_CH = "审核退回";
	public static final String ELECTRONICS_REDBILL_STATUS_305 = "305";
	public static final String ELECTRONICS_REDBILL_STATUS_305_CH = "合并红冲开票中";
	public static final String ELECTRONICS_REDBILL_STATUS_306 = "306";
	public static final String ELECTRONICS_REDBILL_STATUS_306_CH = "单笔红冲开票中";
	public static final String ELECTRONICS_REDBILL_STATUS_307 = "307";
	public static final String ELECTRONICS_REDBILL_STATUS_307_CH = "红冲审核通过";

	public static String getElctronicsDataStatusCH(
			String ElectronicsDataStatus, String bussFlag) {
		if ("ELECTRONICS_TRANS".equalsIgnoreCase(bussFlag)) {
			if (ELECTRONICS_TRANS_STATUS_201.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_TRANS_STATUS_201_CH;
			} else if (ELECTRONICS_TRANS_STATUS_202
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_TRANS_STATUS_202_CH;
			} else if (ELECTRONICS_TRANS_STATUS_203
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_TRANS_STATUS_203_CH;
			} else if (ELECTRONICS_TRANS_STATUS_204
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_TRANS_STATUS_204_CH;
			} else if (ELECTRONICS_TRANS_STATUS_205
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_TRANS_STATUS_205_CH;
			} else if (ELECTRONICS_TRANS_STATUS_206
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_TRANS_STATUS_206_CH;
			}
		} else if ("ELECTRONICS_Bill".equalsIgnoreCase(bussFlag)) {

		}
		else if ("ELECTRONICS_REDBill".equalsIgnoreCase(bussFlag)) {
			if (ELECTRONICS_REDBILL_STATUS_301.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_301_CH;
			} else if (ELECTRONICS_REDBILL_STATUS_302
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_302_CH;
			} else if (ELECTRONICS_REDBILL_STATUS_303
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_303_CH;
			} else if (ELECTRONICS_REDBILL_STATUS_304
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_304_CH;
			} else if (ELECTRONICS_REDBILL_STATUS_305
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_305_CH;
			} else if (ELECTRONICS_REDBILL_STATUS_306
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_306_CH;
			} else if (ELECTRONICS_REDBILL_STATUS_307
					.equals(ElectronicsDataStatus)) {
				return ELECTRONICS_REDBILL_STATUS_307_CH;
			}
		}
		return "";

	}

}
