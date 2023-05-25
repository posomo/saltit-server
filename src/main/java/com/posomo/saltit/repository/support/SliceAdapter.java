package com.posomo.saltit.repository.support;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class SliceAdapter {

	public static <T> Slice<T> makeListToSlice(Pageable pageable,
		List<T> content) {
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(content, pageable, hasNext);
	}
}
