package org.etb.app.components;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.ClientDataEncoder;
import org.apache.tapestry5.services.ClientDataSink;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * 参数组件，用于事件参数提交
 * @author Alex Huang
 * @email 102233492@qq.com
 */
public class Params {
	
	public static class Param {
		private String requestKey;
		private String js;
		private Object obj;
		
		public static Param of(String requestKey, String js){
			Param p = new Param();
			p.requestKey = requestKey;
			p.js = js;
			return p;
		}
		
		public static Param ofObject(String requestKey, Serializable obj){
			Param p = new Param();
			p.requestKey = requestKey;
			p.obj = obj;
			return p;
		}
	}
	
	/**
	 * Params组件id
	 */
    @Parameter(name = "id", defaultPrefix = BindingConstants.LITERAL)
    private String idParameter;
    
    @Parameter(required = true, allowNull = false)
    private List<Param> params;
    
    /**
     * 当Params已于页面存在时，extra=true时可向该参数组件添加额外的参数
     */
    @Parameter
    private boolean extra;
	
	@Environmental
	private JavaScriptSupport jsSupport;
	
	@Inject
	private ClientDataEncoder clientDataEncoder;
	
	void afterRender(){
		if(extra){
			jsSupport.addScript("if(!Etb.Params." + idParameter + "){Etb.Params." + idParameter + "=[];}");		
		}else{
			jsSupport.addScript("Etb.Params." + idParameter + "=[];");
		}
		
		ObjectOutputStream out = null;
		try {
			
			for(Param p : params){
				if(p.js != null){
					jsSupport.addScript("Etb.Params." + idParameter 
							+ ".push({\"key\":\"%s\",\"value\":\"%s\"});", p.requestKey, p.js.replace("\"", "\\\""));
				}else if(p.obj != null){
					
					ClientDataSink sink = clientDataEncoder.createSink();
					out = sink.getObjectOutputStream();
					out.writeObject(p.obj);
					
					jsSupport.addScript("Etb.Params." + idParameter 
							+ ".push({\"key\":\"%s\",\"value\":\"%s\",\"serialize\":true});", p.requestKey, sink.getClientData());
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			//ObjectOutputStream会在getClientData里close掉，这里只防止写入时出错
			InternalUtils.close(out);
		}
	}
}
