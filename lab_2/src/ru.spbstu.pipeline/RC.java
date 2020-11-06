package ru.spbstu.pipeline;

public enum RC {

	CODE_SUCCESS, // ошибки не произошло
	CODE_INVALID_ARGUMENT, // передан невалидный аргумент
	CODE_FAILED_TO_READ, // невозможно прочитать из потока
	CODE_FAILED_TO_WRITE, // невозможно записать в поток
	CODE_INVALID_INPUT_STREAM, // невозможно открыть поток на чтение
	CODE_INVALID_OUTPUT_STREAM, // невозможно открыть поток на запись
	CODE_CONFIG_GRAMMAR_ERROR, // ошибка в грамматике конфига
	CODE_CONFIG_SEMANTIC_ERROR, // семантическая ошибка в конфиге
	CODE_FAILED_PIPELINE_CONSTRUCTION // при конструировании конвейера произошла ошибка
}
