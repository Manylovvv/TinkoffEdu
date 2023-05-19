package ru.tinkoff.edu.java.parser.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.tinkoff.edu.java.parser.records.StackOverflowRecord;

public final class StackOverflowLinkParser extends AbstractLinkParser {
    /**
     * Шаблон выражения имеет одну группу захвата в скобках - question id
     * - ^ соответствует началу строки
     * - https://stackoverflow.com/questions/ соответствует буквальной строке "https://stackoverflow.com/questions/"
     * - (\\d+) соответствует одной или нескольким цифрам и фиксирует их в первой группе
     * - / соответствует символу косой черты
     * - [a-z-\\d]+ соответствует одной или нескольким строчным буквам, дефисам или цифрам.
     * Эта часть шаблона соответствует заголовку вопроса, но не фиксируется.
     */
    private static final String PATTERN_STRING = "^https://stackoverflow.com/questions/(\\d+)/[a-z-\\d]+";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

    public StackOverflowLinkParser(AbstractLinkParser nextLink) {
        super(nextLink);
    }

    /**
     * Метод parseLink использует класс Pattern для сопоставления ссылки с регулярным выражением и извлечения
     * из нее идентификатора вопроса. Затем он возвращает новый объект StackOverflowRecord с извлеченной информацией.
     * Если ссылка не соответствует регулярному выражению, она вызывает метод `parseLink` следующего парсера в цепочке,
     * если он существует.
     */
    @Override
    public Record parseLink(String link) {
        Matcher matcher = PATTERN.matcher(link);
        if (matcher.find()) {
            return new StackOverflowRecord(Long.parseLong(matcher.group(1)));
        }
        return nextParser != null ? nextParser.parseLink(link) : null;
    }
}
