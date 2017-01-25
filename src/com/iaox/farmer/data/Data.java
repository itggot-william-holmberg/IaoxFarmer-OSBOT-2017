package com.iaox.farmer.data;

import java.util.ArrayList;
import java.util.List;

import com.iaox.farmer.data.items.IaoxItem;

public class Data {

	public static final int[] AVAILABLE_F2P_WORLDS = {301,308,316,326,382,383,384};
	public static final int[] AVAILABLE_P2P_WORLDS = {304,305,306,309,310,311,312,313,314,
														317,319,320,321,322,327,328,333,334,
															336,338,341,342,343,344,346,350,
																351,352,354,357,358,359,360,
																	362,367,368,369,370,375,376,377};
	
	//generate one task for every playtime. Playtime is what comes before every break. Play x amount of time bla bla.
	public static final boolean ONE_TASK_PER_PLAYTIME = true;
	public static List<IaoxItem> WITHDRAW_LIST = new ArrayList<IaoxItem>();
	
	public  static boolean trainDefence = true;
	public static long lastHomeTeleport;
	public static boolean USE_BREAKS = true;

}
