package org.etb.app.components.b;

import com.google.code.kaptcha.Producer;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.etb.app.base.BaseClientElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by daodao on 14-5-19.
 * email:46524818@qq.com
 * 验证码组件
 */
public class VerifyCode extends BaseClientElement {

    @Inject
    private Producer captchaProducer;

    @Inject
    private ComponentResources resources;

    public Boolean beginRender(MarkupWriter writer) {
        Link link = resources.createEventLink("verifycode");
        writer.element("img",
                "onclick", "this.src='" + link.toURI() + "?'+Math.random()",
                "src", link.toURI(),
                "title", "点击更换验证码",
                "style", "cursor:pointer",
                "id", getClientId()
        );
        resources.renderInformalParameters(writer);
        writer.end();
        return false;
    }

    @Inject
    private Request request;

    public StreamResponse onVerifyCode() {

        return new StreamResponse() {

            @Override
            public String getContentType() {
                return "image/jpg";
            }

            @Override
            public InputStream getStream() throws IOException {
                // create the text for the image
                String capText = captchaProducer.createText();
                BufferedImage bi = captchaProducer.createImage(capText);

                request.getSession(true).setAttribute("verifycode",capText);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bi, "jpg", os);
                return new ByteArrayInputStream(os.toByteArray());

            }

            @Override
            public void prepareResponse(Response response) {

            }
        };

    }


}
