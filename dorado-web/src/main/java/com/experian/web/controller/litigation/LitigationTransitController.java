package com.experian.web.controller.litigation;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.experian.core.enums.Srv;
import com.experian.core.pojo.R;
import com.experian.core.rest.RestClient;

@Controller
@RequestMapping("/litigation/transit")
public class LitigationTransitController {
	Logger logger = Logger.getLogger(LitigationTransitController.class);

	/**
	 * @param count(线程数)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/executeDb2Root")
	@ResponseBody
	public R<Void> executeDb2Root(int count) {
		return RestClient.get(Srv.litigation, "/litigation/transit/executeDb2Root/{count}", R.class, count);
	}

	/**
	 * @param count(线程数)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/executeDb2HistoryRoot")
	@ResponseBody
	public R<Void> executeDb2HistoryRoot(int count) {
		return RestClient.get(Srv.litigation, "/litigation/transit/executeDb2HistoryRoot/{count}", R.class, count);
	}

	/**
	 * @param count(线程数)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/executeSqlServer")
	@ResponseBody
	public R<Void> executeSqlServer(int count) {
		return RestClient.get(Srv.litigation, "/litigation/transit/executeDb2HistoryRoot/{count}", R.class, count);
	}

}
