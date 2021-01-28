package ru.spbstu.pipeline;

import java.io.FileOutputStream;

public interface IWriter extends IConfigurable, IPipelineStep {
	RC setOutputStream(FileOutputStream fos);
}
