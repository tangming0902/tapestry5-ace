package org.etb.app.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.base.BaseInject;
import org.etb.app.components.b.SubmitValueButton;
import org.etb.app.constants.EtbEventConstants;
import org.etb.app.enums.BgColor;
import org.etb.app.models.CheckboxItem;
import org.etb.app.models.RadioItem;
import org.etb.app.models.StringKV;
import org.etb.app.models.Option;
import org.etb.app.models.SelectModel;
import org.etb.app.services.ui.AjaxBehavior;

/**
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public class Test extends BaseInject {

	@Property
	private String value;

	@Property
	private String byteRangeLengthValue;

	@Property
	private String idcardValue;

	@Property
	private String mobileValue;

	@Property
	private String telephoneValue;

	@Property
	private String phoneValue;

	@Property
	private String zipCodeValue;

	@Property
	private Integer intValue;

	@Property
	private String emailValue;

	@Property
	private Double doubleValue = 111111.1;

	@Property
	private List<CheckboxItem> checkboxData;

	@Property
	private List<RadioItem> radioGroupData;

	@Property
	private String radioGroupValue;

	void beginRender() {

		checkboxData = new ArrayList<CheckboxItem>();
		checkboxData.add(CheckboxItem.of("football", "足球"));
		checkboxData.add(CheckboxItem.of("basketball", "篮球").setSelected(true));

		radioGroupData = new ArrayList<RadioItem>();
		radioGroupData.add(RadioItem.of("male", "男"));
		radioGroupData.add(RadioItem.of("female", "女"));

		selectValue = StringKV.of("male", "男");

		uploads = new ArrayList<StringKV>();
		uploads.add(StringKV.of("fileId1", "预置文件1.jpg"));
		uploads.add(StringKV.of("fileId2", "预置文件2.mp4"));
	}

	@CatchRequestParameter(SubmitValueButton.REQUEST_KEY)
	private String submitValueButtonText;

	@Property
	private List<StringKV> uploads;

	@OnEvent(component = "upload", value = EtbEventConstants.UPLOAD_FILE)
	String uploadFile(UploadedFile file) {
		String fileId = "id-" + file.getFileName();
		System.out.println(fileId);
		return fileId;
	}

	@OnEvent(component = "upload2", value = EtbEventConstants.UPLOAD_FILE)
	String uploadFile2(UploadedFile file) {
		String fileId = "id-" + file.getFileName();
		System.out.println(fileId);
		return fileId;
	}

	@OnEvent(component = "form", value = EtbEventConstants.SUCCESS)
	void formSuccess() {
		System.out.println(uploads);
	}

	@OnEvent(component = "radiogroup", value = EtbEventConstants.CLICK)
	void radiogroup(String value) {
		System.out.println(value);
	}

	@Inject
	private AjaxBehavior ajaxBehavior;

	@Inject
	@Path("classpath:org/etb/app/res/bootstrap/avatars/avatar4.png")
	private Asset avatar;

	@Inject
	private Block notifyBlock;

	@Inject
	private Block dialogTitleBlock;

	@Inject
	private Block dialogButtonsBlock;

	@Inject
	private Block dialogBlock;

	@OnEvent(component = "commonButton", value = EtbEventConstants.CLICK)
	void commonButton() {
		radioGroupData = new ArrayList<RadioItem>();
		radioGroupData.add(RadioItem.of("male", "男"));
		radioGroupData.add(RadioItem.of("female", "女"));
		ajaxBehavior.notify(BgColor.DEFAULT, "您好我是黄宇", notifyBlock,
				avatar.toClientURL());
	}

	@OnEvent(component = "commonButton2", value = EtbEventConstants.CLICK)
	void commonButton2() {
		radioGroupData = new ArrayList<RadioItem>();
		radioGroupData.add(RadioItem.of("male", "男"));
		radioGroupData.add(RadioItem.of("female", "女"));
		ajaxBehavior.showDialog("dialog1", dialogTitleBlock, notifyBlock,
				dialogButtonsBlock);
	}

	@OnEvent(component = "commonButton3", value = EtbEventConstants.CLICK)
	void commonButton3() {
		radioGroupData = new ArrayList<RadioItem>();
		radioGroupData.add(RadioItem.of("male", "男"));
		radioGroupData.add(RadioItem.of("female", "女"));
		ajaxResponseRenderer.addRender("popZone", dialogBlock);
	}

	@Property
	private StringKV selectValue;

	@Property
	private List<StringKV> selectValues;

	public SelectModel getSelectModel() {
		return new SelectModel() {

			public List<Option> getOptions() {
				List<Option> list = new ArrayList<Option>();
				list.add(Option.of("male", "男"));
				list.add(Option.of("female", "女"));
				return list;
			}

		};
	}

	public SelectModel getSelectModel2() {
		return new SelectModel() {

			public List<Option> getOptions() {
				List<Option> list = new ArrayList<Option>();
				list.add(Option.of("1", "天河区"));
				list.add(Option.of("2", "海珠区"));
				list.add(Option.of("3", "荔湾区"));
				list.add(Option.of("4", "白云区"));
				return list;
			}

		};
	}
}
