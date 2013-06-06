package com.amazon;

public interface IEntryReader<Input, Output> {
	Output processRead(Input input);
}
