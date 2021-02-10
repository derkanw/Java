package ru.spbstu.pipeline;

import java.io.FileInputStream;

public interface IReader extends IPipelineStep, IConsumer, IProducer {
	RC setInputStream(FileInputStream fis);
}
