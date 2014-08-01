package org.etb.app.services;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.services.ComponentClassCache;
import org.apache.tapestry5.internal.transform.ReadOnlyComponentFieldConduit;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.ComputedValue;
import org.apache.tapestry5.plastic.FieldConduit;
import org.apache.tapestry5.plastic.InstanceContext;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticField;
import org.apache.tapestry5.services.ClientDataEncoder;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.etb.app.annotations.CatchRequestParameter;
import org.etb.app.annotations.SerializeObject;

public class CatchRequestParameterWorker implements
		ComponentClassTransformWorker2 {

	private Request request;

	private ComponentClassCache classCache;

	private ValueEncoderSource valueEncoderSource;

	private ClientDataEncoder clientDataEncoder;

	public CatchRequestParameterWorker(Request request,
			ComponentClassCache classCache,
			ValueEncoderSource valueEncoderSource,
			ClientDataEncoder clientDataEncoder) {
		this.request = request;
		this.classCache = classCache;
		this.valueEncoderSource = valueEncoderSource;
		this.clientDataEncoder = clientDataEncoder;
	}

	public void transform(PlasticClass plasticClass,
			TransformationSupport support, MutableComponentModel model) {
		for (PlasticField field : plasticClass
				.getFieldsWithAnnotation(CatchRequestParameter.class)) {
			mapFieldToQueryParameter(field, support);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void mapFieldToQueryParameter(PlasticField field,
			TransformationSupport support) {
		CatchRequestParameter annotation = field
				.getAnnotation(CatchRequestParameter.class);

		field.claim(annotation);

		String parameterName = getParameterName(field, annotation);

		SerializeObject annotation2 = field
				.getAnnotation(SerializeObject.class);

		ComputedValue<FieldConduit<Object>> provider = null;
		if (annotation2 == null) {
			Class fieldType = classCache.forName(field.getTypeName());

			ValueEncoder encoder = valueEncoderSource
					.getValueEncoder(fieldType);

			provider = createFieldValueConduitProvider(field, parameterName,
					encoder);
		} else {
			provider = createFieldSerializeValueConduitProvider(field,
					parameterName);
		}

		field.setComputedConduit(provider);
	}

	private String getParameterName(PlasticField field,
			CatchRequestParameter annotation) {
		if (annotation.value().equals(""))
			return field.getName();

		return annotation.value();
	}

	@SuppressWarnings("rawtypes")
	private ComputedValue<FieldConduit<Object>> createFieldValueConduitProvider(
			PlasticField field, final String parameterName,
			final ValueEncoder encoder) {
		final String fieldName = field.getName();

		return new ComputedValue<FieldConduit<Object>>() {
			public FieldConduit<Object> get(InstanceContext context) {
				final ComponentResources resources = context
						.get(ComponentResources.class);

				return new ReadOnlyComponentFieldConduit(resources, fieldName) {
					public Object get(Object instance, InstanceContext context) {
						String clientValue = request
								.getParameter(parameterName);

						if (clientValue == null)
							return null;

						return encoder.toValue(clientValue);
					}
				};
			}
		};
	}

	private <T> ComputedValue<FieldConduit<T>> createFieldSerializeValueConduitProvider(
			PlasticField field, final String parameterName) {
		final String fieldName = field.getName();

		return new ComputedValue<FieldConduit<T>>() {

			public FieldConduit<T> get(InstanceContext context) {

				return new FieldConduit<T>() {

					@SuppressWarnings("unchecked")
					public T get(Object instance, InstanceContext context) {
						Object v = request.getAttribute("p:" + parameterName);
						if (v != null)
							return (T) v;

						String clientData = request.getParameter(parameterName);

						if (clientData == null)
							return null;

						T value = null;
						ObjectInputStream in = null;
						try {
							in = clientDataEncoder.decodeClientData(clientData);
							value = (T) in.readObject();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} finally {
							InternalUtils.close(in);
						}
						request.setAttribute("p:" + parameterName, value);
						return value;
					}

					public void set(Object instance, InstanceContext context,
							T newValue) {
						throw new RuntimeException(String.format(
								"Field %s is read only.", fieldName));
					}
				};
			}
		};
	}
}
