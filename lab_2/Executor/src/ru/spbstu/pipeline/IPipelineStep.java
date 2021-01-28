package ru.spbstu.pipeline;

public interface IPipelineStep extends IExecutable {
	RC setConsumer(IExecutable c);
	RC setProducer(IExecutable p);
}