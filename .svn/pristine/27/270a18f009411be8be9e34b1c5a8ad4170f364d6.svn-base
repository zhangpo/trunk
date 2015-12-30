package cn.com.choicesoft.chinese.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;

import java.io.IOException;

public class JsonUtil {

	private static ObjectMapper mObjectMapper;

	private static ObjectMapper getObjectMapper() {
		if (null == mObjectMapper) {
			mObjectMapper = new ObjectMapper();
		}
		return mObjectMapper;
	}

	public static String getJson(final Object pObject)
			throws IOException {
		return getObjectMapper().writeValueAsString(pObject);
	}

	public static String getJson(final Object pObject, final Class<?> pView)
			throws IOException {
		return getObjectMapper().writerWithView(pView).writeValueAsString(
				pObject);
	}

	public static String getJson(final Object pObject,
			final FilterProvider pFilterProvider)
			throws IOException {
		final ObjectMapper mapper = getObjectMapper();
		mapper.setFilters(pFilterProvider);
		final String json = mapper.writeValueAsString(pObject);
		mapper.setFilters(null);
		return json;
	}

	public static <T> T getObject(final String pJson, final Class<T> pValueType)
			throws IOException {
		return getObjectMapper().readValue(pJson, pValueType);
	}

	public static JsonNode getNode(final String pJson,
			final String... pNodeNames) throws
			IOException {
		JsonNode node = getObjectMapper().readTree(pJson);

		for (String nodeName : pNodeNames) {
			node = node.get(nodeName);
		}

		return node;
	}

	public static <T> T jsonNode2GenericObject(final JsonNode pNode,
			final TypeReference<T> pTr) throws
			IOException {
		if (pNode == null) {
			return null;
		} else {
			ObjectMapper objectMapper = getObjectMapper();
			return objectMapper
					.readValue(objectMapper.treeAsTokens(pNode), pTr);
		}
	}

	public static <T> T getObject(final String pJson,
			final TypeReference<T> pTr, final String... pNodeNames)
			throws
			IOException {
		return jsonNode2GenericObject(getNode(pJson, pNodeNames), pTr);
	}
}
