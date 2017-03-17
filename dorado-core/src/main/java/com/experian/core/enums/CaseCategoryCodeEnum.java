package com.experian.core.enums;

/**
 * 案件分类枚举
 * 
 * @author lixiongcheng
 *
 */
public enum CaseCategoryCodeEnum {
	/**
	 * 裁判文书
	 */
	JudgmentDocuments(1, "01000"),
	/**
	 * 民事案件
	 */
	CivilCase(2, "01100"),
	/**
	 * 刑事案件
	 */
	CriminalCase(3, "01200"),
	/**
	 * 行政案件
	 */
	AdministrativeCase(4, "01300"),
	/**
	 * 赔偿案件
	 */
	CompensationCase(5, "01400"),
	/**
	 * 执行案件
	 */
	EnforcementCase(6, "01500"),
	/**
	 * 执行公告
	 */
	ExecutiveAnnouncement(7, "02000"),
	/**
	 * 失信公告
	 */
	DishonestyAnnouncement(8, "03000"),
	/**
	 * 开庭公告
	 */
	HearingAnnouncement(9, "04000"),
	/**
	 * 法院公告
	 */
	CourtBulletin(10, "05000"),
	/**
	 * 限制高消费，限制出入境，限制招投标
	 */
	HighSpendingImmigrationBiddingLimits(11, "07000"),
	/**
	 * 限制高消费
	 */
	HighSpendingLimits(12, "07100"),
	/**
	 * 限制出入境
	 */
	ImmigrationRestrictions(13, "07200"),
	/**
	 * 限制招投标
	 */
	BiddingRestrictions(14, "07300"),
	/**
	 * 其他
	 */
	other(15, "08000");

	private String code;
	private int id;

	public String getCode() {
		return code;
	}

	public int getId() {
		return id;
	}

	private CaseCategoryCodeEnum(int id, String code) {
		this.id = id;
		this.code = code;
	}

	public static String getCodeById(int id) {
		for (CaseCategoryCodeEnum c : CaseCategoryCodeEnum.values()) {
			if (c.getId() == id) {
				return c.getCode();
			}
		}
		return null;
	}

	public static Integer getIdByCode(String code) {
		for (CaseCategoryCodeEnum c : CaseCategoryCodeEnum.values()) {
			if (c.getCode().equals(code)) {
				return c.getId();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(CaseCategoryCodeEnum.getCodeById(1));
		System.out.println(CaseCategoryCodeEnum.getIdByCode("01000"));
	}
}
