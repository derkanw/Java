package ru.spbstu.pipeline;

import java.util.logging.Level;
import java.util.logging.Logger;

public enum RC {

	CODE_SUCCESS("Work completed successfully"), // ошибки не произошло
	CODE_INVALID_ARGUMENT("No program start arguments"), // передан невалидный аргумент
	CODE_FAILED_TO_READ("File read error"), // невозможно прочитать из потока
	CODE_FAILED_TO_WRITE("File write error"), // невозможно записать в поток
	CODE_INVALID_INPUT_STREAM("Invalid input stream"), // невозможно открыть поток на чтение
	CODE_INVALID_OUTPUT_STREAM("Invalid output stream"), // невозможно открыть поток на запись
	CODE_CONFIG_GRAMMAR_ERROR("Grammar error while reading the config file"), // ошибка в грамматике конфига
	CODE_CONFIG_SEMANTIC_ERROR("Semantic error while reading the config file"), // семантическая ошибка в конфиге
	CODE_FAILED_PIPELINE_CONSTRUCTION("Pipeline construct error"); // при конструировании конвейера произошла ошибка

	private final String message;
	RC(String message)
	{
		this.message = message;
	}

	private Logger logger = Logger.getLogger(RC.class.getName());

	public void getError()
	{
		logger.log(Level.WARNING, message);
	}

	public void getSuccess()
	{
		logger.log(Level.INFO, message);
	}
}
