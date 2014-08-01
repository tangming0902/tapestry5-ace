package org.etb.app.services;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Validator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.meta.MetaWorker;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.upload.services.UploadModule;
import org.etb.app.constants.CoreSymbolConstants;
import org.etb.app.services.stacks.BootstrapStack;
import org.etb.app.services.ui.AjaxBehavior;
import org.etb.app.services.ui.impl.AjaxBehaviorImpl;
import org.etb.app.validators.ByteRangeLength;
import org.etb.app.validators.Email;
import org.etb.app.validators.IdCard;
import org.etb.app.validators.MaxLength;
import org.etb.app.validators.MinLength;
import org.etb.app.validators.Mobile;
import org.etb.app.validators.Phone;
import org.etb.app.validators.StringCheck;
import org.etb.app.validators.Telephone;
import org.etb.app.validators.ZipCode;

@SubModule({ UploadModule.class })
public class AppModule {

	public static void bind(ServiceBinder binder) {
		// 常用UI服务
		binder.bind(AjaxBehavior.class, AjaxBehaviorImpl.class);
	}

	@SuppressWarnings("rawtypes")
	public static void contributeFieldValidatorSource(
			MappedConfiguration<String, Validator> configuration) {
		configuration.override("minlength", new MinLength());
		configuration.override("maxlength", new MaxLength());
		configuration.override("email", new Email());

		configuration.add("stringcheck", new StringCheck());
		configuration.add("byterangelength", new ByteRangeLength());
		configuration.add("idcard", new IdCard());
		configuration.add("mobile", new Mobile());
		configuration.add("telephone", new Telephone());
		configuration.add("phone", new Phone());
		configuration.add("zipcode", new ZipCode());
	}

	@Contribute(ComponentClassTransformWorker2.class)
	@Primary
	public static void provideTransformWorkers(
			OrderedConfiguration<ComponentClassTransformWorker2> configuration,
			MetaWorker metaWorker, ComponentClassResolver resolver) {
		configuration.addInstance("CatchRequestParameterWorker",
				CatchRequestParameterWorker.class);
	}

	public static void contributeJavaScriptStackSource(
			MappedConfiguration<String, JavaScriptStack> conf) {
		conf.addInstance(CoreSymbolConstants.ACE_CORE_STACK,
				BootstrapStack.class);
	}

	// public static void contributeFactoryDefaults(
	// MappedConfiguration<String, Object> configuration) {
	// // The application version number is incorprated into URLs for some
	// // assets. Web browsers will cache assets because of the far future
	// // expires
	// // header. If existing assets are changed, the version number should
	// // also
	// // change, to force the browser to download new versions. This overrides
	// // Tapesty's default
	// // (a random hexadecimal number), but may be further overriden by
	// // DevelopmentModule or
	// // QaModule.
	// configuration.override(SymbolConstants.APPLICATION_VERSION, "1.1");
	// }

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, Object> configuration) throws Exception {
		// Contributions to ApplicationDefaults will override any contributions
		// to
		// FactoryDefaults (with the same key). Here we're restricting the
		// supported
		// locales to just "en" (English). As you add localised message catalogs
		// and other assets,
		// you can extend this list of locales (it's a comma separated series of
		// locale names;
		// the first locale name is the default when there's no reasonable
		// match).

		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,zh_CN");
		configuration.add(SymbolConstants.CHARSET, "UTF-8");
		configuration.add(SymbolConstants.PRODUCTION_MODE, false); // false true
		configuration.add(SymbolConstants.HMAC_PASSPHRASE, "etbHMAC");
		configuration.add(SymbolConstants.COMBINE_SCRIPTS, "false");

		System.setProperty("file.encoding", "UTF-8");
		Locale.setDefault(Locale.CHINA);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Contribute(TypeCoercer.class)
	public static void provideBasicTypeCoercions(
			Configuration<CoercionTuple> configuration) {
		add(configuration, Object.class, String.class,
				new Coercion<Object, String>() {
					public String coerce(Object input) {
						return input.toString();
					}
				});

		add(configuration, Object.class, Boolean.class,
				new Coercion<Object, Boolean>() {
					public Boolean coerce(Object input) {
						return input != null;
					}
				});

		add(configuration, String.class, Double.class,
				new Coercion<String, Double>() {
					public Double coerce(String input) {
						return new Double(input);
					}
				});

		// String to BigDecimal is important, as String->Double->BigDecimal
		// would lose
		// precision.

		add(configuration, String.class, BigDecimal.class,
				new Coercion<String, BigDecimal>() {
					public BigDecimal coerce(String input) {
						return new BigDecimal(input);
					}
				});

		add(configuration, BigDecimal.class, Double.class,
				new Coercion<BigDecimal, Double>() {
					public Double coerce(BigDecimal input) {
						return input.doubleValue();
					}
				});

		add(configuration, String.class, BigInteger.class,
				new Coercion<String, BigInteger>() {
					public BigInteger coerce(String input) {
						return new BigInteger(input);
					}
				});

		add(configuration, String.class, Long.class,
				new Coercion<String, Long>() {
					public Long coerce(String input) {
						return new Long(input);
					}
				});

		add(configuration, Long.class, Byte.class, new Coercion<Long, Byte>() {
			public Byte coerce(Long input) {
				return input.byteValue();
			}
		});

		add(configuration, Long.class, Short.class,
				new Coercion<Long, Short>() {
					public Short coerce(Long input) {
						return input.shortValue();
					}
				});

		add(configuration, Long.class, Integer.class,
				new Coercion<Long, Integer>() {
					public Integer coerce(Long input) {
						return input.intValue();
					}
				});

		add(configuration, Number.class, Long.class,
				new Coercion<Number, Long>() {
					public Long coerce(Number input) {
						return input.longValue();
					}
				});

		add(configuration, Double.class, Float.class,
				new Coercion<Double, Float>() {
					public Float coerce(Double input) {
						return input.floatValue();
					}
				});

		add(configuration, Long.class, Double.class,
				new Coercion<Long, Double>() {
					public Double coerce(Long input) {
						return input.doubleValue();
					}
				});

		add(configuration, String.class, Boolean.class,
				new Coercion<String, Boolean>() {
					public Boolean coerce(String input) {
						String trimmed = input == null ? "" : input.trim();

						if (trimmed.equalsIgnoreCase("false")
								|| trimmed.length() == 0)
							return false;

						// Any non-blank string but "false"

						return true;
					}
				});

		add(configuration, Number.class, Boolean.class,
				new Coercion<Number, Boolean>() {
					public Boolean coerce(Number input) {
						return input.longValue() != 0;
					}
				});

		add(configuration, Void.class, Boolean.class,
				new Coercion<Void, Boolean>() {
					public Boolean coerce(Void input) {
						return false;
					}
				});

		add(configuration, Collection.class, Boolean.class,
				new Coercion<Collection, Boolean>() {
					public Boolean coerce(Collection input) {
						return !input.isEmpty();
					}
				});

		add(configuration, Object.class, List.class,
				new Coercion<Object, List>() {
					public List coerce(Object input) {
						return Collections.singletonList(input);
					}
				});

		add(configuration, Object[].class, List.class,
				new Coercion<Object[], List>() {
					public List coerce(Object[] input) {
						return Arrays.asList(input);
					}
				});

		add(configuration, Object[].class, Boolean.class,
				new Coercion<Object[], Boolean>() {
					public Boolean coerce(Object[] input) {
						return input != null && input.length > 0;
					}
				});

		add(configuration, Float.class, Double.class,
				new Coercion<Float, Double>() {
					public Double coerce(Float input) {
						return input.doubleValue();
					}
				});

		Coercion primitiveArrayCoercion = new Coercion<Object, List>() {
			public List<Object> coerce(Object input) {
				int length = Array.getLength(input);
				Object[] array = new Object[length];
				for (int i = 0; i < length; i++) {
					array[i] = Array.get(input, i);
				}
				return Arrays.asList(array);
			}
		};

		add(configuration, byte[].class, List.class, primitiveArrayCoercion);
		add(configuration, short[].class, List.class, primitiveArrayCoercion);
		add(configuration, int[].class, List.class, primitiveArrayCoercion);
		add(configuration, long[].class, List.class, primitiveArrayCoercion);
		add(configuration, float[].class, List.class, primitiveArrayCoercion);
		add(configuration, double[].class, List.class, primitiveArrayCoercion);
		add(configuration, char[].class, List.class, primitiveArrayCoercion);
		add(configuration, boolean[].class, List.class, primitiveArrayCoercion);

		add(configuration, String.class, File.class,
				new Coercion<String, File>() {
					public File coerce(String input) {
						return new File(input);
					}
				});

		add(configuration, Object.class, Object[].class,
				new Coercion<Object, Object[]>() {
					public Object[] coerce(Object input) {
						return new Object[] { input };
					}
				});

		add(configuration, Collection.class, Object[].class,
				new Coercion<Collection, Object[]>() {
					public Object[] coerce(Collection input) {
						return input.toArray();
					}
				});
	}

	@SuppressWarnings("rawtypes")
	private static <S, T> void add(Configuration<CoercionTuple> configuration,
			Class<S> sourceType, Class<T> targetType, Coercion<S, T> coercion) {
		configuration.add(CoercionTuple
				.create(sourceType, targetType, coercion));
	}
}
