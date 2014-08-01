package org.etb.app.validators;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.validator.AbstractValidator;
import org.etb.app.utils.StringUtils;

/**
 * 字节长度范围<br>
 * 例:byteRangeLength=5-10
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class ByteRangeLength extends AbstractValidator<String, String> {

	public ByteRangeLength() {
		super(String.class, String.class, "byte-range-length");
	}

	public void render(Field field, String constraintValue,
			MessageFormatter formatter, MarkupWriter markupWriter,
			FormSupport formSupport) {
		Integer[] constraintValueArr = getConstraintValueArr(constraintValue);
		JSONObject json = new JSONObject();
		json.put("min", constraintValueArr[0]);
		json.put("max", constraintValueArr[1]);
		
		formSupport.addValidation(field, "byteRangeLength",
				buildMessage(formatter, field, constraintValueArr),
				json);
	}

	private String buildMessage(MessageFormatter formatter, Field field,
			Integer[] constraintValue) {
		return formatter.format(field.getLabel(), constraintValue[0],
				constraintValue[1]);
	}

	public void validate(Field field, String constraintValue,
			MessageFormatter formatter, String value)
			throws ValidationException {
		Integer[] constraintValueArr = getConstraintValueArr(constraintValue);
		int length = length(value);
		if (length < constraintValueArr[0] || length > constraintValueArr[1])
			throw new ValidationException(buildMessage(formatter, field,
					constraintValueArr));
	}
	
	private Integer[] getConstraintValueArr(String constraintValue){
		String[] arr = constraintValue.split("-");
		return new Integer[]{Integer.valueOf(arr[0]), Integer.valueOf(arr[1])};
	}

	private int length(String value) {
		if (StringUtils.isEmpty(value))
			return 0;

		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}
}
