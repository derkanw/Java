package ru.spbstu.pipeline;

public interface IPipelineStep extends IConfigurable {
	RC setConsumer(IConsumer c);
	RC setProducer(IProducer p);
}