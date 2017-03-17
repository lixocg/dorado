package com.experian.daas.litigation.handler;

import com.experian.comp.rabbitmq.ConsumerHandler;
import com.experian.daas.litigation.service.LitigationAdapterService;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;
import com.google.gson.Gson;

public class DeplicateHandler implements ConsumerHandler {
	private LitigationAdapterService litigationAdapterService;

	public DeplicateHandler(LitigationAdapterService litigationAdapterService) {
		this.litigationAdapterService = litigationAdapterService;
	}

	@Override
	public void handle(String msg) {
		LitigationAPIPojo litigation = new Gson().fromJson(msg, LitigationAPIPojo.class);
		litigationAdapterService.executeAdapt(litigation);
	}

}
