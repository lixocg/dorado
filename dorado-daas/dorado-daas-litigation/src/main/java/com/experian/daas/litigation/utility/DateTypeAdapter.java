package com.experian.daas.litigation.utility;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {
	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.nullValue();
		} else {
			out.value(value.getTime());
		}
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == null || in.peek() == JsonToken.NULL) {
			in.skipValue();
			return null;
		}
		String str = in.nextString();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = format1.parse(str);
		} catch (ParseException e) {
			try {
				d = format2.parse(str);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return d;
	}


}
