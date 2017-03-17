package com.experian.web.controller.litigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.experian.core.constant.LitiBusiConstant;
import com.experian.core.enums.Srv;
import com.experian.core.pojo.R;
import com.experian.core.rest.RestClient;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;

@Controller
@RequestMapping("litigation")
public class LitigationController {

	@RequestMapping("oldDataTransfer")
	public String oldDataTransfer() {
		return "litigation/oldDataTransfer";
	}

	@RequestMapping("dereplicate")
	public String dereplicate() {
		return "litigation/dereplicate";
	}

	@RequestMapping("batchDereplicate")
	public String batchDereplicate() {
		return "litigation/batchDereplicate";
	}

	@RequestMapping(value = "/getPojo", method = RequestMethod.POST)
	@ResponseBody
	public R<LitigationAPIPojo> getPojo(@RequestParam("id") String id) {
		LitigationAPIPojo res = RestClient.get(Srv.litigation,
				"/litigation/adapter/getAPiPojoById/{detailId}/{fetchState}/{parsedStatus}", LitigationAPIPojo.class,
				id, LitiBusiConstant.DataFetchStatus.UN_FETCH, LitiBusiConstant.DataStatus.SUCCESS);
		return new R<LitigationAPIPojo>(res);
	}

	@RequestMapping("executeDureplicate")
	@ResponseBody
	public R<String> executeDureplicate(String id) {
		LitigationAPIPojo res = RestClient.get(Srv.litigation,
				"/litigation/adapter/getAPiPojoById/{detailId}/{fetchState}/{parsedStatus}", LitigationAPIPojo.class,
				id, LitiBusiConstant.DataFetchStatus.UN_FETCH, LitiBusiConstant.DataStatus.SUCCESS);
		@SuppressWarnings("unchecked")
		R<String> ress = RestClient.post(Srv.litigation, "/litigation/adapter/executeAdapt", res, R.class, "");
		return ress;
	}

	@RequestMapping("/startDeduplicate")
	@ResponseBody
	public R<String> startDeduplicate(@RequestParam String num) {
		long _num = Long.parseLong(num);
		RestClient.get(Srv.litigation, "/litigation/adapter/startDeduplicate/{num}", R.class, _num);
		return new R<String>("started");
	}

	@RequestMapping("/stopDeduplicate")
	@ResponseBody
	public R<String> stopDeduplicate() {
		RestClient.get(Srv.litigation, "/litigation/adapter/stopDeduplicate", R.class);
		return new R<String>("stopped");
	}

	@RequestMapping(value = "/oldDataDeplidate", method = RequestMethod.POST)
	public R<Void> oldDataDeplidate() {
		RestClient.get(Srv.litigation, "/litigation/adapter/oldDataDeplidate", R.class);
		return new R<Void>();
	}
}
