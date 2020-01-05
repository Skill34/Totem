package com.skilldev.obsitotem.utils;

public interface Callback<T> {
	public void call(Throwable t, T result);
}
