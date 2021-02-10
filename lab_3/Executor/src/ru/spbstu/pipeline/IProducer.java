package ru.spbstu.pipeline;

public interface IProducer {
    TYPE[] getOutputTypes();
    IMediator getMediator(TYPE type);
}
