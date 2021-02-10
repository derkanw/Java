package ru.spbstu.pipeline;

import java.io.FileOutputStream;

public interface IWriter extends IPipelineStep, IConsumer {
	RC setOutputStream(FileOutputStream fos);
}
