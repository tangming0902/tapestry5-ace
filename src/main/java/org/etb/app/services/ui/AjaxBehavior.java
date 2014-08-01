package org.etb.app.services.ui;

import org.etb.app.enums.BgColor;

/**
 * 全局的Ajax行为，比如消息提示、弹出Dialog
 * 
 * @author AlexHuang
 * @email huangyu407@qq.com
 */
public interface AjaxBehavior {

	public void notify(BgColor color, Object title, Object text,
			String imageUrl, boolean sticky);

	public void notify(BgColor color, Object title, Object text, String imageUrl);

	public void notify(BgColor color, Object title, Object text);

	public void error(Object title, Object text);

	public void success(Object title, Object text);

	public void info(Object title, Object text);

	public void showDialog(String dialogId, Object title, Object content,
			Object buttons);

	public void closeDialog(String dialogId);
}
